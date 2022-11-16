const url = "http://localhost:8080/admin2/";
const tbody = document.getElementById('tbody');
let users = [];

const renderUsers = async (users) => {
    const response = await fetch(url);

    if (response.ok) {
        let json = await response.json()
            .then(data => getAllUsers(data));
    } else {
        alert("Ошибка HTTP: " + response.status);
    }


function getAllUsers(users) {

    let output = ''
    users.forEach(user => {
        output += ` 
              <tr> 
                    <td>${user.id}</td> 
                    <td>${user.firstname}</td> 
                    <td>${user.lastname}</td> 
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => " " + role.name.substring(5))}</td> 
                    
              <td> 
                   <button type="button" class="btn btn-info" id="edit-user" data-action="edit" 
                    data-id="${user.id}" data-toggle="modal" data-target="#editModal" data-userid="${user.id}" >Edit</button> 
               </td> 
               <td> 
                   <button type="button" class="btn btn-danger" id="delete-user" data-action="delete" 
                   data-id="${user.id}" data-target="#deleteModal">Delete</button> 
                    </td> 
              </tr>`
        })
    tbody.innerHTML = output;
    }
}
// ALL users

const updateUser = (user) => {
    const foundIndex = users.findIndex(x => x.id === user.id);
    users[foundIndex] = user;
    renderUsers(users);
    console.log('users');
}
const removeUser = (id) => {
    users = users.filter(user => user.id !== id);
    console.log(users)
    renderUsers(users);

}

fetch(url, {mode: 'cors'})
    .then(res => res.json())
    .then(data => {
        users = data;
        renderUsers(data)
    })

//Заполнение черной полосы навигационного бара

$(async function() {
    await thisAdmin();
});
async function thisAdmin() {
    fetch("http://localhost:8080/user2")
        .then(res => res.json())
        .then(data => {
            $('#navAdmin').append(data.username);
            let roleList = data.roles.map(role => " " + role.name.substring(5));
            $('#adminRole').append(roleList);

        })
}