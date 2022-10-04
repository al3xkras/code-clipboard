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
        template.setAttribute("id","sample"+i);
        i++;
        if (i%2===0){
            codeArea.style.color="cyan"
        }
        console.log(template)
        console.log(codeArea)
        codeArea.innerHTML=sample.codeString;
        template.hidden=null;
        codeView.appendChild(template);
    });
}