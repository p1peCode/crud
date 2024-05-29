$(document).ready(function() {
    $.ajax({
        url: '/user/me',
        method: 'GET',
        dataType: 'json',
        success: function(user) {
            console.log(user);
            userNavigationPanel(user);
            let roles = user.roles.map(role => role.role).join(', ');
            let weatherInfo = user.humidity ? "&#x2602;" : "&#x2600;";
            let userRow = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.address}</td>
                    <td>${weatherInfo}</td>
                    <td>${roles}</td>
                </tr>
            `;
            $('#currentUser').html(userRow);
        },
        error: function(error) {
            console.error('Error of loading user:', error);
        }
    });
});

function userNavigationPanel(user) {
    let email = `<strong>${user.email}</strong>`;
    let roles = user.roles.map(role => role.role.replace('ROLE_', '')).join(', ');
    let content = `${email} with roles: ${roles}`;
    $("#userWithRoles").html(content);
}