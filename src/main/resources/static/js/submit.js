const langSelect=document.getElementById("language");
const codeInput=document.getElementById("code");
const tagsInput=document.getElementById("tags");

const tagsToggle=document.getElementById("tags-toggle");

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
toggleSwitch(tagsToggle,tagsInput);

document.getElementById("submit-button").addEventListener("click", e=>{
    const t = tagsToggle.checked;
    const data = new FormData();
    if (langSelect.selectedIndex>0){
        const lang = langSelect.options[langSelect.selectedIndex].getAttribute("name");
        data.append("language",lang);
    }
    const code = codeInput.value;
    data.append("code",code);

    if (t){
        let tags = ""+tagsInput.value;
        data.append("tags",tags);
    }

    const xhr = new XMLHttpRequest();
    xhr.overrideMimeType("application/json");
    xhr.open('POST', '/send-code', true);
    xhr.onload = function () {
        console.log(xhr.status);
        if (xhr.status===200){
            alert("submitted successfully")
            tagsInput.value="";
            codeInput.value="";
            langSelect.selectedIndex=0;
        } else {
            alert(xhr.status)
        }
    };
    xhr.send(data);
});