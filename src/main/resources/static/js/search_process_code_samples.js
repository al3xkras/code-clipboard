let codeSamples;
const codeView=document.getElementById("code-samples");
function clearCodeTemplates(){
    codeView.innerHTML='';
}
function codeShow(node){
    let codeArea = node.parentNode.parentNode.querySelectorAll('[name="codeArea"]')[0];
    let codeImage = node.parentNode.parentNode.querySelectorAll('[name="codeImage"]')[0];
    codeArea.hidden=null
    codeImage.hidden=true
    node.hidden=true
}
const processCodeSamples=function () {
    let i=1;
    let googleSearch = document.getElementById("google-search-toggle").checked
    googleSearch = googleSearch && pageNum===0 && !extendedSearchDisable
    if (codeSamples.length===0){
        if (substring && substringSearch) {
            if (extendedSearch) {
                if (!pageNumSaved)
                    pageNum=0
                tags=substring
                substring=""
                substringSearch=false
                tagSearch=true
                extendedSearchDisable=true
                searchButton.click()
                return;
            } else if (substringSearch){
                if (!pageNumSaved && googleSearch){
                    window.open('https://google.com/search?q='+substring);
                }
            }
        } else if (tags && tagSearch) {
            if (extendedSearch) {
                if (!pageNumSaved)
                    pageNum=0
                substring=tags
                tags=""
                substringSearch=true
                tagSearch=false
                extendedSearchDisable=true
                searchButton.click()
                return;
            } else if (tagSearch){
                if (!pageNumSaved && googleSearch){
                    window.open('https://google.com/search?q='+tags);
                }
            }
        } else {
            alert("Empty response")
        }
        return
    } else {
        clearCodeTemplates();
    }
    codeSamples.forEach(sample=>{
        let template = document.querySelector('#code-sample').cloneNode(true);
        let codeArea = template.querySelectorAll('[name="codeArea"]')[0];
        let codeImage = template.querySelectorAll('[name="codeImage"]')[0];
        let codeShowButton = template.querySelectorAll('[name="btnShowCode"]')[0];

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

        if (sample.hideCodeText){
            codeArea.hidden=true
            codeShowButton.hidden=null
        }

        if (sample.codeImage){
            codeImage.src="data:image/png;base64,"+sample.codeImage;
            codeImage.hidden=null
        }
    });
}