$.fn.Cart = function (options) {
    return this.each(function (index, el) {

        var defaults = $.extend({
            emptyMessage: "Your shopping cart is empty",
            currencySymbol: "&#164;"
        });

        options = $.extend(defaults, options);

        console.log(options.currencySymbol);

        let cart = _getCart();
        let cartIsEmpty = (cart ? true : false);
        var $this = $(this);

        // get the latest product details from the database
        var size = Object.keys(cart).length;
        if (size > 0) {
            cartIsEmpty = false;
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
                        _addProductRow(updatedProduct);
                        _updateTotals();
                    })
            });

            $('#item-table').on('input', function () {
                const row = $(this).closest('tr');
                const pid = row.attr('id');
                alert($(this).val());
            });

            $('#item-table').on('click', 'button', function () {
                const row = $(this).closest('tr');
                const pid = row.attr('id');
                if ($(this).hasClass("js-btn-minus")) {
                    _subtractItemFromCart(pid, 1);
                } else if ($(this).hasClass("js-btn-plus")) {
                    _addItemToCart(pid, 1);
                } else {
                    console.error("fn.Cart::unknown click event")
                }
            });

            $('#item-table').on('click', 'a', function () {
                const row = $(this).closest('tr');
                const pid = row.attr('id');
                row.remove();
                _removeItemFromCart(pid);
                _updateTotals();
            });

            $this.find('#update-cart').on('click', function (event) {
                _updateTotals();
            });
            $this.find('#continue-shopping').on('click', function (event) {
                window.location.href = "/products";
            });
            $this.find('#checkout-cart').on('click', function (event) {
                window.location.href = "/cart/checkout";
            });

            $('#item-cart').toggleClass("d-none");
            $('#action-cart').toggleClass("d-none");
        } else {
            cartIsEmpty = true;
        }

        if (cartIsEmpty) {
            $('#empty-cart').toggleClass("d-none");
        }
    });

    function _addProductRow(product) {
        var imageFile = (product.image ? product.image : 'awaiting-image-sm.png');
        $('#item-table>tbody').append(
            "<tr id='" + product.id + "'>" +
            "<td class='product-thumbnail'>" +
            "<img src='/img/products/" + imageFile + "' alt='image' class='img-fluid'>" +
            "</td>" +
            "<td class='product-name'>" +
            "<h2 class='h5 text-black'>" + product.name + "</h2>" +
            "</td>" +
            "<td>" + options.currencySymbol + "<span class='product-price'>" + Number(product.price).toFixed(2) + "</span></td>" +
            "<td>" +
            "<div class='input-group mx-auto' style='max-width: 150px;'>" +
            "<div class='input-group-prepend'>" +
            "<button class='btn btn-outline-primary js-btn-minus' type='button'>&minus;</button>" +
            "</div>" +
            "<input class='product-quantity form-control text-center' value='" + product.quantity + "' readonly/>" +
            "<div class='input-group-append'>" +
            "<button class='btn btn-outline-primary js-btn-plus' type='button'>&plus;</button>" +
            "</div>" +
            "</div>" +
            "</td>" +
            "<td>" + options.currencySymbol + "<span class='product-total'>" + Number(product.total).toFixed(2) + "</span></td>" +
            "<td><a href='#' id='remove-product' class='btn btn-primary height-auto btn-sm'>X</a></td>" +
            "</tr>"
        )
    }

    function _updateTotals() {
        let subTotal = 0.0;
        $('#item-table>tbody tr').each(function () {
            let self = $(this);
            let productPrice = self.find("span.product-price").text().trim();
            let productQty = self.find("input.product-quantity").val().trim();
            let productTotal = Number(productPrice * productQty);
            self.find("span.product-total").html(productTotal.toFixed(2));
            subTotal = subTotal + productTotal;
        })
        let subTotalStr = String(options.currencySymbol).concat(Number(subTotal).toFixed(2));
        $('#cart-subtotal').html(subTotalStr);
        $('#cart-total').html(subTotalStr);
    }

    function _removeItemFromCart(pid) {
        let cart = [];
        if (localStorage.getItem('cart')) {
            try {
                cart = JSON.parse(localStorage.getItem('cart'));
            } catch (e) {
                console.error("fn.Cart::Error retrieving cart from localStorage");
                localStorage.removeItem('cart');
                return;
            }
        }
        const index = cart.findIndex(x => x.pid === pid);
        if (index >= 0) {
            cart.splice(index, 1);
        }
        const cartCount = cart.reduce(function (a, b) {
            return a + parseInt(b.quantity);
        }, 0);
        const parsed = JSON.stringify(cart);
        localStorage.setItem('cart', parsed);
        $(document).trigger("updateCartCount", [cartCount]);
        if (cartCount == 0) {
            $('#item-cart').toggleClass("d-none");
            $('#empty-cart').toggleClass("d-none");
        }
    }

    function _subtractItemFromCart(pid, count) {
        let cart = _getCart();
        const index = cart.findIndex(x => x.pid === pid);
        if (index >= 0) {
            let item = cart.find(x => x.pid === pid);
            let qty = item.quantity;
            if (qty === 1) {
                _removeItemFromCart(pid);
            } else if (qty > 1) {
                item.quantity = (qty - 1);
                $('tr#' + pid).find("input.product-quantity").val(item.quantity);
                _updateCart(cart);
                _updateTotals();
            }
        } else {
            console.error("fn.Cart::Product id: " + pid + " not found in cart local storager");
        }
    }

    function _addItemToCart(pid, count) {
        let cart = _getCart();
        const index = cart.findIndex(x => x.pid === pid);
        if (index >= 0) {
            let item = cart.find(x => x.pid === pid);
            let qty = item.quantity;
            item.quantity = (qty + 1);
            $('tr#' + pid).find("input.product-quantity").val(item.quantity);
            _updateCart(cart);
            _updateTotals();
        } else {
            console.error("fn.Cart::Product id: " + pid + " not found in cart local storager");
        }
    }

    function _updateCart(cart) {
        const parsed = JSON.stringify(cart);
        localStorage.setItem('cart', parsed);
        const cartCount = cart.reduce(function (a, b) {
            return a + parseInt(b.quantity);
        }, 0);
        $(document).trigger("updateCartCount", [cartCount]);
        if (cartCount == 0) {
            $('#item-cart').toggleClass("d-none");
            $('#empty-cart').toggleClass("d-none");
        }
    }

    function _getCart() {
        let cart = [];
        if (localStorage.getItem('cart')) {
            try {
                cart = JSON.parse(localStorage.getItem('cart'));
            } catch (e) {
                console.error("fn.Cart::Error retrieving cart from localStorage");
                localStorage.removeItem('cart');
                return null;
            }
        }
        return cart;
    }
};
