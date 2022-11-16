
on(document, 'click', '#edit-user', e => {

    const userInfo = e.target.parentNode.parentNode

    document.getElementById('idEdit').value = userInfo.children[0].innerHTML
    document.getElementById('firstnameEdit').value = userInfo.children[1].innerHTML
    document.getElementById('lastnameEdit').value = userInfo.children[2].innerHTML
    document.getElementById('ageEdit').value = userInfo.children[3].innerHTML
    document.getElementById('emailEdit').value = userInfo.children[4].innerHTML
    document.getElementById('usernameEdit').value = userInfo.children[5].innerHTML
    document.getElementById('passwordEdit').value = userInfo.children[6].innerHTML;

    $("#editModal").modal("show")


})

const editUserForm = document.querySelector('#editModal')

editUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const formData = new FormData(document.getElementById('formEdit'));
    const object = {
        roles: []
    };

    formData.forEach((value, key) => {
        if (key === "nameRoles"){

            const roleId = value.split("_")[0];
            const roleName = value.split("_")[1];
            const role = {
                id : roleId,
                name : "ROLE_" + roleName
            };
            object.roles.push(role);
        } else {
            object[key] = value;
        }
    });

    fetch(url+document.getElementById('idEdit').value, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)
    })
        .then(res => res.json()).then(data => updateUser(data))
        .catch((e) => console.error(e))

    $("#editModal").modal("hide");
})