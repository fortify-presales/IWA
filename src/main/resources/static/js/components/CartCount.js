$.fn.CartCount = function (options) {
    return this.each(function (index, el) {

        var defaults = $.extend({
            color: "red"
        });

        options = $.extend(defaults, options);

        var count = 0;
        var $this = $(this);

        if (localStorage.getItem('cart')) {
            try {
                const cart = JSON.parse(localStorage.getItem('cart'));
                const cartCount = cart.reduce(function (a, b) {
                    return a + parseInt(b.quantity);
                }, 0);
                count = cartCount;
            } catch (e) {
                console.log("Error retrieving cart from localStorage");
            }
        }

        $this.text(count);

        $(document).on("updateCartCount", function (event, cartCount) {
            $this.text(cartCount);
        });
    });

};
