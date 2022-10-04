$(document).ready(function() {
    var ctrlDown = false,
        ctrlKey = 17,
        cmdKey = 91,
        vKey = 86,
        cKey = 67;

    const searchRef=document.getElementById("search-ref");
    const submitRef=document.getElementById("submit-ref");


    let ctrlParam=false;

    $(document).keydown(function(e) {
        if (e.keyCode === ctrlKey || e.keyCode === cmdKey) ctrlDown = true;
    }).keyup(function(e) {
        if (e.keyCode === ctrlKey || e.keyCode === cmdKey) {
            ctrlDown = false;
            ctrlParam=false;
        }
    });

    let url = new URL(document.URL);
    if (url.searchParams.get("ctrl")){
        ctrlParam=true;
    }

    $(".no-copy-paste").keydown(function(e) {
        if (ctrlDown && (e.keyCode === vKey || e.keyCode === cKey)) return false;
    });

    // Document Ctrl + C/V
    $(document).keydown(function(e) {
        if ((ctrlParam || ctrlDown) && (e.keyCode === cKey)) {
            ctrlParam=false
            let url1=new URL(searchRef.href);
            url1.searchParams.append('ctrl', 1);
            searchRef.href=url1.toString();
            searchRef.click();
        }
        if ((ctrlParam || ctrlDown) && (e.keyCode === vKey)) {
            ctrlParam=false
            let url1=new URL(submitRef.href);
            url1.searchParams.append('ctrl', 1);
            submitRef.href=url1.toString();
            submitRef.click();
        }
    });
});