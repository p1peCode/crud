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
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${roles}</td>
                            <td><button type="button" class="btn btn-info btn-edit" data-id="${user.id}" data-toggle="modal" data-target="#ModalEditUser">Edit</button></td>
                            <td><button class="btn btn-danger btn-delete" data-id="${user.id}" data-toggle="modal" data-target="#ModalDeleteUserCentral">Delete</button></td>
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
                        console.log(user);
                        let form = $('#modalEditUserForm');
                        // Заполнение полей формы данными пользователя
                        form.find('#ModalInputId').val(user.id);
                        form.find('#ModalInputFirstName').val(user.name);
                        form.find('#ModalInputLastName').val(user.lastName);
                        form.find('#ModalInputAge').val(user.age);
                        form.find('#ModalInputEmail').val(user.email);
                        //form.find('#ModalInputRole').val(user.roles.map(r => r.role).join(', '));
                        let roleSelect = form.find('#ModalInputRole');
                        roleSelect.empty();
                        ['ROLE_ADMIN', 'ROLE_USER'].forEach(role => {
                            let isSelected = user.roles.some(userRole => userRole.role === role);
                            roleSelect.append(new Option(role, role, isSelected, isSelected));
                        });

                        $('#ModalEditUser').modal('show');
                    },
                    error: function (error) {
                        console.error("error of loading user:", error);
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
                        $('#ModalLastNameDelete').val(user.lastName);
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
    let roleInput = $('#InputRole');
    let roles;
    if (roleInput.is('select') && roleInput.prop('multiple')) {
        roles = roleInput.val();
        roles = roles.map(role => role.trim());
    } else {
        roles = roleInput.val().split(',').map(role => role.trim());
    }
    let newUser = {
        name: $('#InputFirstName').val(),
        lastName: $('#InputLastName').val(),
        age: $('#InputAge').val(),
        email: $('#InputEmail').val(),
        password: $('#InputPassword').val(),
        roles: roles
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
    let selectedRoles = $('#ModalInputRole').val();
    let updatedUser = {
        id: userId,
        name: $('#ModalInputFirstName').val(),
        lastName: $('#ModalInputLastName').val(),
        age: $('#ModalInputAge').val(),
        email: $('#ModalInputEmail').val(),
        password: $('#ModalInputPassword').val(),
        roles: selectedRoles.map(role => ({ role: role.trim() }))
        //roles: $('#ModalInputRole').val().split(',').map(role => role.trim())
    };

    console.log('Updating user:', updatedUser);

    $.ajax({
        url: '/admin/users/' + userId,
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
        url: '/admin/users/' + userId,
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
