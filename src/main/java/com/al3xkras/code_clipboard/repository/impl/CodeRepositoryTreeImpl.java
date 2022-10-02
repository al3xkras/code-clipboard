package com.al3xkras.code_clipboard.repository.impl;

import com.al3xkras.code_clipboard.entity.Code;
import com.al3xkras.code_clipboard.model.ProgrammingLanguage;
import com.al3xkras.code_clipboard.model.suffixtree.GeneralizedSuffixTree;
import com.al3xkras.code_clipboard.repository.CodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.al3xkras.code_clipboard.CodeClipboardApplication.delimiter;

@Slf4j
@Repository
public class CodeRepositoryTreeImpl implements CodeRepository {

    private String storageDir = "/tmp/storage/";
    private String suffixTreePath = storageDir+"tree.bin";
    private String tmpStoragePath = storageDir+"code.bin";
    public static final int DEFAULT_STORAGE_SIZE = (int) 1e6;

    private GeneralizedSuffixTree suffixTree;
    private HashMap<Integer,Code> temporaryStorage;

    public CodeRepositoryTreeImpl(){
        String prefix = new FileSystemResource("").getFile().getAbsolutePath().replaceAll("\\\\","/");
        storageDir=prefix+storageDir;
        suffixTreePath=prefix+suffixTreePath;
        tmpStoragePath=prefix+tmpStoragePath;
        log.info(suffixTreePath);
        load();
    }

    protected GeneralizedSuffixTree loadTree() throws IOException, ClassNotFoundException {
        GeneralizedSuffixTree suffixTree;
        Path p = Paths.get(suffixTreePath);
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p));
            suffixTree = (GeneralizedSuffixTree) ois.readObject();
        } catch (Exception e) {
            String error = "failed to load tree (path: "+p+" )";
            log.error(error);
            throw e;
        }
        return suffixTree;
    }
    protected HashMap<Integer,Code> loadCode() throws IOException, ClassNotFoundException {
        HashMap<Integer,Code> storage;
        Path p = Paths.get(tmpStoragePath);
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p));
            storage = (HashMap<Integer,Code>) ois.readObject();
        } catch (Exception e) {
            String error="failed to load temporary storage (path: "+p+" )";
            log.error(error);
            throw e;
        }
        return storage;
    }

    @Override
    public void serialize() throws IOException {
        new File(storageDir).mkdirs();
        new File(suffixTreePath).createNewFile();
        ObjectOutputStream tree = new ObjectOutputStream(Files.newOutputStream(Paths.get(suffixTreePath)));
        tree.writeObject(suffixTree);
        tree.close();
        new File(tmpStoragePath).createNewFile();
        ObjectOutputStream storage = new ObjectOutputStream(Files.newOutputStream(Paths.get(tmpStoragePath)));
        storage.writeObject(temporaryStorage);
        storage.close();
    }

    @Override
    public void load() {
        try {
            this.temporaryStorage=loadCode();
        } catch (IOException e) {
            this.temporaryStorage=new HashMap<>(DEFAULT_STORAGE_SIZE);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (this.temporaryStorage.isEmpty()){
            this.suffixTree=new GeneralizedSuffixTree();
            return;
        }
        try {
            this.suffixTree=loadTree();
        } catch (IOException e){
            this.suffixTree=new GeneralizedSuffixTree();
            HashMap<Integer,Code> storage=temporaryStorage;
            temporaryStorage=new HashMap<>(DEFAULT_STORAGE_SIZE);
            for (Code code: storage.values()){
                save(code,Collections.emptyList());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Code save(Code code, List<String> tags) {
        List<String> l = new LinkedList<>();
        l.add(code.getLanguage().toString().toLowerCase());
        tags.forEach(t->l.add(t.toLowerCase()));
        l.add(code.toString().toLowerCase());

        String codeString = String.join(delimiter,l);
        int index = temporaryStorage.size();
        code.setId((long)index);
        temporaryStorage.put(index,code);
        suffixTree.put(codeString,index);
        return code;
    }

    @Override
    public Code deleteById(Long id) {
        return temporaryStorage.remove(Math.toIntExact(id));
    }

    @Override
    public List<Code> findAllByTags(Collection<String> tags) {
        HashSet<Integer> query = null;
        for (String tag: tags){
            if (query==null){
                query=new HashSet<>(suffixTree.search(tag));
                continue;
            }
            query.retainAll(suffixTree.search(tag));
            if (query.isEmpty())
                break;
        }
        if (query==null)
            return Collections.emptyList();

        return query.stream().map(i->findById((long)i).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Optional<Code> findById(Long id){
        return Optional.ofNullable(temporaryStorage.get(Math.toIntExact(id)));
    }

    @Override
    public List<Code> findAllByTagsAndLanguage(Collection<String> tags, ProgrammingLanguage language) {
        List<String> tagsList = new LinkedList<>(tags);
        tags.add(language.toString().toLowerCase()+delimiter);
        return findAllByTags(tagsList);
    }

    @Override
    public List<Code> findAllBySubstring(String substring) {
        if (substring.contains(delimiter))
            throw new IllegalArgumentException("invalid query string: \""+substring+'\"');
        return findAllByTags(Collections.singletonList(substring));
    }

    @PreDestroy
    void preDestroy() throws IOException {
        serialize();
    }
}
