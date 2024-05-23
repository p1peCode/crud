function loadUsers() {
    $.ajax({
        url: '/admin/allUsers',
        type: 'GET',
        dataType: 'json',
        success: function (users) {
            let tbody = $('#allUsers');
            tbody.empty();

            users.forEach(function (user) {
                let roles = user.roles.map(role => role.role).join(', ');

                let userRow = `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.lastname}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${roles}</td>
                            <td><button class="btn btn-info btn-edit" data-id="${user.id}">Edit</button></td>
                            <td><button class="btn btn-danger btn-delete" data-id="${user.id}">Delete</button></td>
                        </tr>
                    `;
                tbody.append(userRow);
            });

            $('.btn-edit').click(function () {
                let userId = $(this).data('id');
                $.ajax({
                    url: '/admin/users/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (user) {
                        // модальное окно редактирования пользователя
                        $('#ModalInputId').val(user.id);
                        $('#ModalInputFirstName').val(user.name);
                        $('#ModalInputLastName').val(user.lastname);
                        $('#ModalInputAge').val(user.age);
                        $('#ModalInputEmail').val(user.email);
                        $('#ModalInputRole').val(user.roles.map(r => r.role).join(', '));
                        $('#ModalEditUser').modal('show');
                    }
                });
            });

            $('.btn-delete').click(function () {
                let userId = $(this).data('id');
                $.ajax({
                    url: '/admin/users/' + userId,
                    type: 'GET',
                    dataType: 'json',
                    success: function (user) {
                        // модальное окно удаления пользователя
                        $('#ModalIdDelete').val(user.id);
                        $('#ModalFirstNameDelete').val(user.name);
                        $('#ModalLastNameDelete').val(user.lastname);
                        $('#ModalAgeDelete').val(user.age);
                        $('#ModalEmailDelete').val(user.email);
                        $('#ModalRoleDelete').val(user.roles.map(r => r.role).join(', '));
                        $('#ModalDeleteUserCentral').modal('show');
                    }
                });
            });
        },
        error: function (error) {
            console.error("Error of getting users:", error);
        }
    });
}

loadUsers();

// форма добавления нового пользователя
$('#newUserForm').submit(function (event) {
    event.preventDefault();
    let newUser = {
        name: $('#InputFirstName').val(),
        lastname: $('#InputLastName').val(),
        age: $('#InputAge').val(),
        email: $('#InputEmail').val(),
        password: $('#InputPassword').val(),
        roles: $('#InputRole').val().split(',').map(role => role.trim())
    };
    $.ajax({
        url: './admin',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(newUser),
        success: function () {
            loadUsers();
            $('#newUserForm')[0].reset();
        },
        error: function (error) {
            console.error("Error of adding user:", error);
        }
    });
});

// обработчик кнопки сохранения редактирования пользователя
$('#saveEditUser').click(function () {
    let userId = $('#ModalInputId').val();
    let updatedUser = {
        id: userId,
        name: $('#ModalInputFirstName').val(),
        lastname: $('#ModalInputLastName').val(),
        age: $('#ModalInputAge').val(),
        email: $('#ModalInputEmail').val(),
        password: $('#ModalInputPassword').val(),
        roles: $('#ModalInputRole').val().split(',').map(role => role.trim())
    };
    $.ajax({
        url: '/admin/' + userId,
        type: 'PATCH',
        contentType: 'application/json',
        data: JSON.stringify(updatedUser),
        success: function () {
            $('#ModalEditUser').modal('hide');
            loadUsers();
        },
        error: function (error) {
            console.error("Error of updating user:", error);
        }
    });
});

// Обработчик кнопки подтверждения удаления пользователя
$('#confirmDeleteUser').click(function () {
    let userId = $('#ModalIdDelete').val();
    $.ajax({
        url: '/admin/' + userId,
        type: 'DELETE',
        success: function () {
            $('#ModalDeleteUserCentral').modal('hide');
            loadUsers();
        },
        error: function (error) {
            console.error("Error of deleting user:", error);
        }
    });
});
