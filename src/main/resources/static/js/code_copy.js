function copy(text) {
    const input = document.createElement('textarea');
    input.innerHTML = text;
    document.body.appendChild(input);
    input.select();
    const result = document.execCommand('copy');
    document.body.removeChild(input);
    return result;
}
function codeCopy(node) {
    copy(node.parentNode.querySelectorAll('[name="codeArea"]')[0].innerHTML)
}