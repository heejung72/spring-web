var main = {
    init: function () {
        const btnSave = document.querySelector('#btn-save');
        if (btnSave) {
            btnSave.addEventListener('click', () => this.save());
        }

        const btnUpdate = document.querySelector('#btn-update');
        if (btnUpdate) {
            btnUpdate.addEventListener('click', () => this.update());
        }

        const btnDelete = document.querySelector('#btn-delete');
        if (btnDelete) {
            btnDelete.addEventListener('click', () => this.delete());
        }
    },

    save: function () {
        const data = {
            title: document.querySelector('#title').value,
            content: document.querySelector('#content').value,
            author: document.querySelector('#author').value
        };

        fetch('/api/v1/posts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(() => {
                alert("Your post is registered!");
                window.location.href = '/';
            })
            .catch(err => alert(JSON.stringify(err)));
    },

    update: function () {
        const data = {
            title: document.querySelector('#title').value,
            content: document.querySelector('#content').value
        };

        const id = document.querySelector('#id').value;

        fetch('/api/v1/posts/' + id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(() => {
                alert("Your post is updated!");
                window.location.href = '/';
            })
            .catch(err => alert(JSON.stringify(err)));
    },

    delete: function () {
        const id = document.querySelector('#id').value;

        fetch('/api/v1/posts/' + id, {
            method: 'DELETE'
        })
            .then(() => {
                alert("Your post is deleted!");
                window.location.href = '/';
            })
            .catch(err => alert(JSON.stringify(err)));
    }
};

document.addEventListener('DOMContentLoaded', () => {
    main.init();
});
