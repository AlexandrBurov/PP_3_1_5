
const addUserForm = document.querySelector('#profile');

addUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const addForm = document.getElementById("addForm");
    const formData = new FormData(addForm);
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


    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)
    })
        .then(res => res.json())
        .then(data => updateUser(data))
        .then(() => addForm.reset())
        .catch((e) => console.error(e))



// Переход на таблицу
    $('#userTable-tab').click();

})

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e)
        }
    })
}

function setSuccessMessage(message) {
    document.getElementById("message").innerHTML = message;
}



