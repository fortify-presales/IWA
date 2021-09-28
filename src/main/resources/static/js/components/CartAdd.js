$.fn.CartAdd = function (options) {
    return this.each(function (index, el) {

        var defaults = $.extend({
            pid: "",
            quantity: 1
        });

        options = $.extend(defaults, options);

        var cart = [];
        var pid = options.pid;
        var quantity = options.quantity;
        var $this = $(this), $quantityInput = $this.find('#quantity-value');

        if (localStorage.getItem('cart')) {
            try {
                cart = JSON.parse(localStorage.getItem('cart'));
            } catch (e) {
                console.log("Error retrieving cart from localStorage");
                localStorage.removeItem('cart');
            }
        }

        $this.find('#quantity-minus').on('click', function () {
            if (quantity > 1) {
                quantity--;
                $quantityInput.val(quantity);
            }
        });
        $this.find('#quantity-plus').on('click', function () {
            quantity++;
            $quantityInput.val(quantity);

        });
        $this.find('#quantity-value').on('input', function () {
            quantity = $(this).val();
        });
        $this.find('#add-to-cart').on('click', function (event) {
            event.preventDefault();
            const newItem = { pid:pid, quantity: quantity };
            const index = cart.findIndex(x => x.pid === pid);
            if (index >= 0) {
                cart.splice(index, 1);
            }
            cart.push(newItem);
            _saveCart(cart);
            _showCheckoutNowModal("The items have been added to your shopping cart.", "text-success");
        });
    });

    function _showCheckoutNowModal(text, cssClass) {
        var checkoutModalH5 = document.createElement("h5"); checkoutModalH5.classList.add(cssClass); checkoutModalH5.innerHTML = text;
        var checkoutModalDiv = document.createElement("div"); checkoutModalDiv.classList.add("m-4", "text-center");
        checkoutModalDiv.appendChild(checkoutModalH5);
        $('#checkout-now-modal').find('#checkout-now-modal-body').empty().append(checkoutModalDiv);
        $('#checkout-now-modal').find('#checkout-now-modal-footer .btn').on('click', function (event) {
            if (this.id === "checkout-now") {
                window.location.href = '/cart';
            }
        });
        $('#checkout-now-modal').modal('toggle');
        return checkoutModalDiv;
    }

    function _saveCart(cart) {
        const cartCount = cart.reduce(function (a, b) {
            return a + parseInt(b.quantity);
        }, 0);;
        const parsed = JSON.stringify(cart);
        localStorage.setItem('cart', parsed);
        $(document).trigger("updateCartCount", [cartCount]);
    }

};
