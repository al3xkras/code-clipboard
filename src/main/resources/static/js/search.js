const langToggle=document.getElementById("language-toggle");
const substringToggle=document.getElementById("substring-toggle");
const tagsToggle=document.getElementById("tags-toggle");

const langSelect=document.getElementById("language")
const substringInput=document.getElementById("substring")
const tagDelimiterInput=document.getElementById("tag-delim")
const tagsInput=document.getElementById("tags")

let extendedSearch = document.getElementById("extended-search-toggle").checked
let extendedSearchDisable = false;
let substring = ""
let tags = ""
let substringSearch = substringToggle.checked
let tagSearch = tagsToggle.checked
let pageNumSaved;

const toggleSwitch = function (inputCheckbox,...elements) {
    inputCheckbox.addEventListener("click",e=>{
        if (inputCheckbox.checked){
            elements.forEach(e=>{
                e.disabled=null;
            });
        } else {
            elements.forEach(e=>{
                e.disabled=1;
            });
        }
    })
}
toggleSwitch(langToggle,langSelect);
toggleSwitch(substringToggle,substringInput);
toggleSwitch(tagsToggle,tagDelimiterInput,tagsInput);

document.getElementById("search-button").addEventListener("click", e=>{
    if (extendedSearchDisable){
        extendedSearch=false
        console.log("disabled")
    } else {
        substring = substringInput.value
        tags = tagsInput.value
        substringSearch = substringToggle.checked
        tagSearch = tagsToggle.checked
        pageNumSaved=pageNum
        console.log("set")
        console.log(substringSearch)
        console.log(tagSearch)
    }
    console.log(substring)
    console.log(tags)

    pageSize=pageSizeInput.value;
    const l = langToggle.checked;
    const s = substringSearch;
    const t = tagSearch;
    if (l && !s && !t){
        alert("Unable to search code by programming language only");
        return;
    } else  if (!l && !s && !t){
        alert("Please choose at least one search option");
        return;
    }
    const data = new FormData();
    if (l){
        const lang = langSelect.options[langSelect.selectedIndex].getAttribute("name");
        data.append("language",lang);
    }
    if (s){
        data.append("substring",substring);
    }
    if (t){
        let delim = " ";//tagDelimiterInput.value
        if (delim.length===0) {
            delim=" ";
        }
        console.log(delim)
        let tags1 =tags.replaceAll(new RegExp(delim,'g')," ");
        data.append("tags",tags1);
    }
    if (pageNum!=null)
        data.append("page",""+pageNum)
    if (pageSize!=null)
        data.append("size",""+pageSize)

    const xhr = new XMLHttpRequest();
    xhr.overrideMimeType("application/json");
    xhr.open('POST', '/search', true);
    xhr.onload = function () {
        if (xhr.status===200){
            codeSamples=JSON.parse(xhr.responseText);
            processCodeSamples()
        } else {
            alert(xhr.status)
        }
    };
    xhr.send(data);
    if (!extendedSearchDisable){
        extendedSearch = document.getElementById("extended-search-toggle").checked
    } else {
        extendedSearchDisable=false
    }
});