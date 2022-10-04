function deleteEntity(node,id) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/delete/'+id, true);
    xhr.onload = function () {
        console.log(xhr.status);
        if (xhr.status===200){
            alert("code deleted successfully")
            node.parentNode.remove();
        } else {
            alert(xhr.status)
        }
    };
    xhr.send();
}
function codeDelete(node) {
    console.log(node.parentNode.getAttribute("entity-id"))
    deleteEntity(node,node.parentNode.getAttribute("entity-id"));
}