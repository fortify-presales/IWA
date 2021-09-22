$.fn.CartSummary = function (options) {
    return this.each(function (index, el) {

        var defaults = $.extend({
            emptyMessage: "Your shopping cart is empty",
            currencySymbol: "&#164;"
        });

        options = $.extend(defaults, options);

        let cart = _getCart();
        let updatedCart = [];
        let cartIsEmpty = (cart ? true : false);
        let subTotal = 0.0;

        // get the latest product details from the database
        var size = Object.keys(cart).length;
        if (size > 0) {
            cartIsEmpty = false;
            let subtotal = 0.0;
            $.each(cart, function (i, product) {
                $.get("/api/v3/products/" + product.pid)
                    .then(response => {
                        let price = response.price;
                        if (response.onSale) {
                            price = response.salePrice;
                        }
                        const updatedProduct = {
                            id: product.pid,
                            quantity: product.quantity,
                            image: response.image,
                            name: response.name,
                            price: price,
                            total: Number(price * (product.quantity))

                        };
                        updatedCart.push(updatedProduct);
                        // set form hidden field to raw cart value
                        $('input[id=jsonCart]').val(JSON.stringify(updatedCart));
                        _addCartRow(updatedProduct);
                        subTotal = Number(subTotal) + Number(updatedProduct.total);
                        $('input[id=orderAmount]').val(subTotal);
                        let subTotalStr = String(options.currencySymbol).concat(Number(subTotal).toFixed(2));
                        $('#cart-subtotal').html(subTotalStr);
                        $('#order-total').html(subTotalStr);
                        // set hidden field to total
                    })
            });

            $('#item-cart').toggleClass("d-none");
        } else {
            cartIsEmpty = true;
        }

        if (cartIsEmpty) {
            $('#empty-cart').toggleClass("d-none");
        }
    });

    function _addCartRow(product) {
        $('#item-table>tbody').append(
            "<tr id='" + product.id + "'>" +
                "<td>" + product.name + "<strong class='mx-2'>x</strong><span class='product-quantity text-xs-right'>" + product.quantity + "</span></td>" +
                "<td><span class='product-price text-xs-right'></span>" + String(options.currencySymbol).concat(Number(product.price).toFixed(2)) + "</span></td>" +
            "</tr>"
        )
    }

    function _getCart() {
        let cart = [];
        if (localStorage.getItem('cart')) {
            try {
                cart = JSON.parse(localStorage.getItem('cart'));
            } catch (e) {
                console.error("fn.CartSummary::Error retrieving cart from localStorage");
                localStorage.removeItem('cart');
                return null;
            }
        }
        return cart;
    }
};
