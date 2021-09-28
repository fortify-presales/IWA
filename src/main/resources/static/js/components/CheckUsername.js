$.fn.CheckUsername = function (options) {
    return this.each(function (index, el) {

        var settings = $.extend({
            invalidMessage: "The username is already taken",
        });

        var $this = $(this), $usernameInput = $this.find('input[name=username]');
        $this.find('#check-username-button').on('click', async function () {
            let $usernameVal = $usernameInput.val();
            if ($usernameVal) {
                await _usernameTaken($usernameVal).then((exists) => {
                    if (exists) {
                        $('#username-available').addClass("d-none");
                        $('#username-taken').removeClass("d-none");
                        $('#username-taken > p').html("The username <strong>" + $usernameVal + "</strong> is already taken")
                    } else {
                        $('#username-taken').addClass("d-none");
                        $('#username-available').removeClass("d-none");
                        $('#username-available > p').html("The username <strong>" + $usernameVal + "</strong> is available")
                    }
                });
            }
        });
    });

    async function _usernameTaken(username) {
        try {
            return await $.get(`/api/v3/site/username-already-exists/${username}`);
        } catch (error) {
            console.log('Error', error.message)
            return false;
        }
    }

};
