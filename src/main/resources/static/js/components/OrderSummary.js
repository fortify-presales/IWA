$.fn.OrderSummary = function (options) {
    return this.each(function (index, el) {

        console.log(options);

        var defaults = $.extend({
            cart: "",
            emptyMessage: "Your order was empty",
            currencySymbol: "&#164;"

        });

        options = $.extend(defaults, options);

        let order = [];
        try {
            order = JSON.parse(options.cart);
        } catch (e) {
            console.error("fn.OrderSummary::Error parsing cart from page");
        }
        let orderIsEmpty = (order ? true : false);

        // get the latest product details from the database
        let size = Object.keys(order).length;
        if (size > 0) {
            orderIsEmpty = false;
            $.each(order, function (i, product) {
                console.log(product)
                $('#item-table>tbody').append(
                    "<tr id='" + product.id + "'>" +
                    "<td>" + product.name + "<strong class='mx-2'>x</strong><span class='product-quantity text-xs-right'>" + product.quantity + "</span></td>" +
                    "<td><span class='product-price text-xs-right'></span>" + String(options.currencySymbol).concat(Number(product.price).toFixed(2)) + "</span></td>" +
                    "</tr>"
                )
            });

            $('#item-order').toggleClass("d-none");
        } else {
            orderIsEmpty = true;
        }

        if (orderIsEmpty) {
            $('#empty-order').toggleClass("d-none");
        }
    });
    
};
