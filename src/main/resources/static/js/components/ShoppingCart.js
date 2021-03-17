module.exports = {
    name: 'shopping-cart',
    props: {
        'currency': String
    },
    data: function () {
        return {
            currencySymbol: '$',
            cartIsEmpty: true,
            cart: [],
            subTotal: 0,
            total: 0
        };
    },
    async created() {
        // retrieve cart object from localstorage
        let lsCart = [];
        if (localStorage.getItem('cart')) {
            try {
                lsCart = JSON.parse(localStorage.getItem('cart'));
            } catch (e) {
                console.log("Error retrieving cart from localStorage");
                localStorage.removeItem('cart');
            }
        }
        // get the latest product details from the database
        const size = Object.keys(lsCart).length;
        if (size > 0) {
            this.cartIsEmpty = false;
            lsCart.forEach(obj => {
                console.log("shopping-cart::retrieving latest product details for product id: " + obj.id);
                axios
                    .get("/api/v3/products/" + obj.id)
                    .then(response => {
                        const responseItem = response.data;
                        let price = responseItem.price;
                        if (responseItem.onSale) {
                            price = responseItem.salePrice;
                        }
                        const newItem = {
                            id: obj.id,
                            quantity: obj.quantity,
                            image: responseItem.image,
                            name: responseItem.name,
                            price: price,
                            total: Number(price * (obj.quantity))

                        };
                        this.cart.total += newItem.total;
                        this.cart.push(newItem);
                    })
            })
        }
    },
    methods: {
        getItemQuantity: function (itemId) {
            let item = this.cart.find(x => x.id === itemId);
            return Number(item.quantity);
        },
        setItemQuantity: function (itemId, qty) {
            let item = this.cart.find(x => x.id === itemId);
            const index = this.cart.findIndex(x => x.id === itemId);
            item.quantity = qty;
            item.total = Number(item.quantity * item.price)
            Vue.set(this.cart, index, item);
        },
        incrementQuantity: function (itemId) {
            this.setItemQuantity(itemId, Number(this.getItemQuantity(itemId) + 1))
        },
        decrementQuantity: function (itemId) {
            let qty = this.getItemQuantity(itemId);
            if (qty > 0) {
                this.setItemQuantity(itemId, Number(qty - 1));
            }
        },
        quantityTextEntered: function (e, itemId) {
            const inputVal = e.target.value;
            if (isNaN(inputVal)) alert("Please enter a valid number");
            else {
                let qty = parseInt(inputVal);
                let item = this.cart.find(x => x.id === itemId);
                const index = this.cart.findIndex(x => x.id === itemId);
                if (qty >= 0) {
                    this.setItemQuantity(itemId, qty);
                }
            }
        },
        removeItem(itemId) {
            const index = this.cart.findIndex(x => x.id === itemId);
            if (index >= 0) {
                this.cart.splice(index, 1);
            }
            this.saveCart();
        },
        saveCart() {
            let sCart = [];
            let cartCount = 0;
            this.cart.forEach(obj => {
                const newItem = {
                    id: obj.id,
                    name: obj.name,
                    price: obj.price,
                    quantity: obj.quantity,
                };
                cartCount = cartCount + Number(obj.quantity);
                sCart.push(newItem);
            })
            // remove old cart
            localStorage.removeItem('cart');
            const parsed = JSON.stringify(sCart);
            localStorage.setItem('cart', parsed);
            // send update cart count event
            this.$root.$emit('updateCartCount', cartCount);
        },
        updateCartCount() {
            const cartCount = this.cart.reduce((sum, obj) => sum + parseInt(obj['quantity']), 0);
            this.$root.$emit('updateCartCount', cartCount);
        },
        continueShopping() {
            window.location.href = "/products";
        },
        updateCart() {
            // just reload for now
            location.reload();
            //this.$forceUpdate();
        },
        checkoutCart() {
            this.saveCart();
            window.location.href = "/cart/checkout";
        }
    },
    computed: {},
    watch: {
        cart: function (val) {
            this.subTotal = this.cart.reduce((sum, item) => sum + Number(item.price * item.quantity), 0);
            // TODO: calculate any discounts
            this.total = this.subTotal;
            // update cart count
            this.updateCartCount();
        }
    },
    mounted() {
        if (this.$props.currency) this.currencySymbol = this.$props.currency;
    }
};
