$(document).ready(function () {
    loadEmployees();
    addEmployee();
    updateEmployee();
});

function loadEmployees() {
    clearEmployeeTable();
    var employeeRows = $('#employeeRows');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/employees',
        success: function (employees) {
            $.each(employees, function (index, employee) {
                var name = employee.firstName + ' ' + employee.lastName;
                var email = employee.email;
                var age = employee.age;
                var employeeId = employee.id;

                var row = '<tr>';
                row += '<td>' + name + '</td>';
                row += '<td>' + email + '</td>';
                row += '<td>' + age + '</td>';
                row += '<td><button type="button" class="btn btn-info" onclick="showEditForm(' + employeeId + ')">Edit</button></td>';
                row += '<td><button type="button" class="btn btn-danger" onclick="deleteEmployee(' + employeeId + ')">Delete</button></td>';
                row += '</tr>';

                employeeRows.append(row);
            });
        },
        error: function () {
            $('#errorMessages').append($('<li>'))
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text('Error calling web service. Please try again later.');
        }
    });
}

function addEmployee() {
    $('#addButton').click(function (event) {
        var haveValidationErrors = checkAndDisplayValidationErrors($('#addForm').find('input'));

        if (haveValidationErrors) {
            return false;
        }

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/employees',
            data: JSON.stringify({
                firstName: $('#addFirstName').val(),
                lastName: $('#addLastName').val(),
                age: $('#addAge').val(),
                email: $('#addEmail').val(),
                monthlySalary: $('#addMonthlySalary').val()
            }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'dataType': 'json',
            success: function () {
                $('#errorMessages').empty();
                $('#addFirstName').val('');
                $('#addLastName').val('');
                $('#addAge').val('');
                $('#addEmail').val('');
                $('#addMonthlySalary').val('');
                loadEmployees();
            },
            error: function () {
                $('#errorMessages').append($('<li>'))
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text('Error calling web service. Please try again later.');
            }
        });
    });
}

function clearEmployeeTable() {
    $('#employeeRows').empty();
}

function showEditForm(employeeId) {
    $('#errorMessages').empty();

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/employees/' + employeeId,
        success: function (data, status) {
            $('#editFirstName').val(data.firstName);
            $('#editLastName').val(data.lastName);
            $('#editAge').val(data.age);
            $('#editEmail').val(data.email);
            $('#editMonthlySalary').val(data.monthlySalary);
            $('#editEmployeeId').val(data.id);
        },
        error: function () {
            $('#errorMessages').append($('<li>'))
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text('Error calling web service. Please try again later.');
        }
    });

    $('#employeeTableDiv').hide();
    $('#editFormDiv').show();
}

function hideEditForm() {
    $('#errorMessages').empty();

    $('#editFirstName').val('');
    $('#editLastName').val('');
    $('#editAge').val('');
    $('#editEmail').val('');
    $('#editMonthlySalary').val('');

    $('#employeeTableDiv').show();
    $('#editFormDiv').hide();
}

function updateEmployee() {
    $('#updateButton').click(function (event) {
        var haveValidationErrors = checkAndDisplayValidationErrors($('#editForm').find('input'));
        
        if(haveValidationErrors) {
            return false;
        }

        $.ajax({
            type: 'PUT',
            url: 'http://localhost:8080/employees/' + $('#editEmployeeId').val(),
            data: JSON.stringify({
                id: $('#editEmployeeId').val(),
                firstName: $('#editFirstName').val(),
                lastName: $('#editLastName').val(),
                age: $('#editAge').val(),
                email: $('#editEmail').val(),
                monthlySalary: $('#editMonthlySalary').val()
            }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'dataType': 'json',
            success: function () {
                $('#errorMessage').empty();
                hideEditForm();
                loadEmployees();
            },
            error: function () {
                $('#errorMessages').append($('<li>'))
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text('Error calling web service. Please try again later.');
            }
        });
    });
}

function deleteEmployee(employeeId) {
    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:8080/employees/' + employeeId,
        success: function () {
            loadEmployees();
        }
    });
}

function checkAndDisplayValidationErrors(input) {
    $('#errorMessages').empty();

    var errorMessages = [];

    input.each(function () {
        // console.log(this.validity);
        // console.log(this.id);
        // console.log(this.validationMessage);
        if (!this.validity.valid) {
            var errorField = $('label[for=' + this.id + ']').text();
            errorMessages.push(errorField + ' ' + this.validationMessage);
        }
    });

    if (errorMessages.length > 0) {
        $.each(errorMessages, function (index, message) {
            $('#errorMessages').append($('<li>').attr({ class: 'list-group-item list-group-item-danger' }).text(message));
        });
        // return true, indicating that there were errors
        return true;
    } else {
        // return false, indicating that there were no errors
        return false;
    }
}