let searchButton = document.getElementById("search-button");
$(document).ready(function() {
    var ctrlDown = false,
        ctrlKey = 17,
        cmdKey = 91,
        zKey = 90;

    let homeRef=document.getElementById("main-page-ref");
    let ctrlParam=false;

    $(document).keydown(function (e) {
        if (e.keyCode === ctrlKey || e.keyCode === cmdKey) ctrlDown = true;
        else if (e.keyCode === 13) {
            searchButton.click()
        }
    }).keyup(function (e) {
        if (e.keyCode === ctrlKey || e.keyCode === cmdKey) {
            ctrlDown = false;
            ctrlParam=false;
        }
    });

    let url = new URL(document.URL);
    if (url.searchParams.get("ctrl")){
        ctrlParam=true;
    }

    $(document).keydown(function (e) {
        if ((ctrlParam || ctrlDown) && (e.keyCode === zKey)) {
            ctrlParam=false

            let url1=new URL(homeRef.href);
            url1.searchParams.append('ctrl', 1);
            homeRef.href=url1.toString();
            homeRef.click();
        }
    });
});