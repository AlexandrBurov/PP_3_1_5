
let deleteUserId = null;

const deleteUserForm = document.getElementById('formDelete');

deleteUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    e.stopPropagation();
    fetch(url + deleteUserId, {
        method: 'DELETE'
    })
        .then(res => res.json())
        .then(data => {
            removeUser(deleteUserId);
            deleteUserForm.removeEventListener('submit', () => {
            });

            $("#deleteModal").modal("hide")
        })
})


on(document, 'click', '#delete-user', e => {

    const userDeleteInfo = e.target.parentNode.parentNode

    deleteUserId = userDeleteInfo.children[0].innerHTML

    document.getElementById('idDelete').value = userDeleteInfo.children[0].innerHTML
    document.getElementById('firstnameDelete').value = userDeleteInfo.children[1].innerHTML
    document.getElementById('lastnameDelete').value = userDeleteInfo.children[2].innerHTML
    document.getElementById('ageDelete').value = userDeleteInfo.children[3].innerHTML
    document.getElementById('emailDelete').value = userDeleteInfo.children[4].innerHTML
    document.getElementById('roleDelete').value = userDeleteInfo.children[5].innerHTML
    document.getElementById('usernameDelete').value = userDeleteInfo.children[6].innerHTML

    $("#deleteModal").modal("show")
})