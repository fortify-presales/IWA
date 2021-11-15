$.fn.ProductReviews = function (options) {
    return this.each(function (index, el) {

        var defaults = $.extend({
            limit: 10
        });
        options = $.extend(defaults, options);

        var $this = $(this), $data = $this.find('.review-data');
        _getProductReviews(options.pid, options.limit).then(response => {
            $data.empty();
            if (response.length > 0) {
                $('#review-count').text(response.length);
                $.each(response, function (i, row) {
                    let review = _reviewDiv(row);
                    console.log(review);
                    $data.append(review);
                });
            } else {
                $data.append("<div class='col-12'>No reviews found</div>");
            }
        });
    });

    function _reviewDiv(review) {
        return (
            "<div class='col-sm-12 item mb-4'>" +
                "<h6 class='text-dark pt-2'>Review by <em>" + review.user.username + "</em> on " +
                    moment(review.reviewDate).format('MMMM Do YYYY') +
                "</h6>" +
                "<p>" + review.comment + "</p>" +
            "</div>"
        );
    }

    async function _getProductReviews(pid, limit) {
        return await $.get(`/api/v3/reviews?pid=${pid}&limit=${limit}`).then();
    }

};
