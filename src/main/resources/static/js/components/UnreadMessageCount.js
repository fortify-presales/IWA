$.fn.UnreadMessageCount = function (options) {
    return this.each(function (index, el) {

        var defaults = $.extend({
            count: 0
        });

        options = $.extend(defaults, options);
        var count = options.count;
        var self = this
        console.log("Using JWT Bearer token: " + jwtToken);

        $.ajax({
            type: "GET",
            url: "/api/v3/messages/unread-count/" + userId,
            headers: {
                Authorization: 'Bearer ' + jwtToken
            },
            dataType: 'json',
            success: function (result, status, xhr) {
                self.count = result;
                $(el).text(result);
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });

    });

};
