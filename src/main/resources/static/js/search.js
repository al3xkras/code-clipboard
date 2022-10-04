const langToggle=document.getElementById("language-toggle");
const substringToggle=document.getElementById("substring-toggle");
const tagsToggle=document.getElementById("tags-toggle");

const langSelect=document.getElementById("language")
const substringInput=document.getElementById("substring")
const tagDelimiterInput=document.getElementById("tag-delim")
const tagsInput=document.getElementById("tags")

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
    const l = langToggle.checked;
    const s = substringToggle.checked;
    const t = tagsToggle.checked;
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
        console.log(lang)
        data.append("language",lang);
    }
    if (s){
        const substr = substringInput.value;
        data.append("substring",substr);
    }
    if (t){
        let delim = " ";//tagDelimiterInput.value
        if (delim.length===0) {
            delim=" ";
        }
        console.log(delim)
        let tags = ""+tagsInput.value;
        tags=tags.replaceAll(new RegExp(delim,'g')," ");
        data.append("tags",tags);
    }

    const xhr = new XMLHttpRequest();
    xhr.overrideMimeType("application/json");
    xhr.open('POST', '/search', true);
    xhr.onload = function () {
        console.log(xhr.status);
        if (xhr.status===200){
            codeSamples=JSON.parse(xhr.responseText);
            processCodeSamples()
        } else {
            alert(xhr.status)
        }
    };
    xhr.send(data);
});