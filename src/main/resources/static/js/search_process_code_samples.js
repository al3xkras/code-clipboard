let codeSamples;
const codeView=document.getElementById("code-samples");
function clearCodeTemplates(){
    codeView.innerHTML='';
}
const processCodeSamples=function () {
    clearCodeTemplates();
    let i=1;
    codeSamples.forEach(sample=>{
        let template = document.querySelector('#code-sample').cloneNode(true);
        let codeArea = template.querySelectorAll('[name="codeArea"]')[0];
        template.setAttribute("id","sample"+sample.codeId);
        template.setAttribute("entity-id",""+sample.codeId);
        i++;
        if (i%2===0){
            codeArea.style.color="cyan"
        }
        codeView.appendChild(template);
        found++
        template.hidden=null;

        codeArea.innerHTML=sample.codeString;
        const maxHeight=250
        codeArea.style.height = ""
        if (codeArea.scrollHeight<=maxHeight){
            codeArea.style.height = codeArea.scrollHeight+"px"
        } else {
            codeArea.style.height = maxHeight+"px"
        }
    });
}