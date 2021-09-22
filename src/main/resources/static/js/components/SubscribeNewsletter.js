$.fn.SubscribeNewsletter = function (options) {
    return this.each(function (index, el) {

        var settings = $.extend({
            color: "#556b2f",
            backgroundColor: "white"
        });

        var $this = $(this), $email = $this.find('#email-subscribe-input');
        $this.find('#email-subscribe-button').on('click', function () {
            if (_validateEmail($email.val())) {
                _saveEmail($email.val()).then(response => {
                    if (response.success) {
                        _showConfirmationText("Thankyou your email address '" + $email.val() + "' has been registered.", "text-success");
                    } else {
                        _showConfirmationText("There was an error registering your email address.", "text-danger");
                    }
                }).catch(error => {
                    _showConfirmationText("There was an error registering your email address.", "text-danger");
                });
            } else {
                _showConfirmationText("Please supply a valid email address.", "text-danger");
            }
        });
    });

    function _showConfirmationText(text, cssClass) {
        var confirmationH5 = document.createElement("h5"); confirmationH5.classList.add(cssClass); confirmationH5.innerHTML = text;
        var confirmationDiv = document.createElement("div"); confirmationDiv.classList.add("m-4", "text-center");
        confirmationDiv.appendChild(confirmationH5);
        $('#confirmation-modal').find('#confirmation-modal-body').empty().append(confirmationDiv);
        $('#confirmation-modal').modal('toggle');
        return confirmationDiv;
    }

    async function _saveEmail(email) {
        return await $.post("/api/v3/site/subscribeUser", { firstName: "", lastName: "", email: email }).then();
    }

    function _validateEmail(email) {
        var emailExpression = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        return emailExpression.test(email);
    }
};
