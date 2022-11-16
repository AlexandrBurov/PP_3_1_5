$(async function() {
    await thisUser();
});
async function thisUser() {
    fetch("http://localhost:8080/user2")
        .then(res => res.json())
        .then(data => {
            $('#navigateUsername').append(data.username);
            let roleList = data.roles.map(role => " " + role.name.substring(5));
            $('#userRole').append(roleList);

            let user = `$(
            <tr>
                <td>${data.id}</td>
                <td>${data.firstname}</td>
                <td>${data.lastname}</td>
                <td>${data.age}</td>
                <td>${data.email}</td>
                <td>${roleList}</td>)`;
            $('#tbodyUsers').append(user);
        })
}





