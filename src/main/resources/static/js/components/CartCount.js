module.exports = {
    name: 'shopping-cart-count',
    data: function () {
        return {
            count: 0
        };
    },
    methods: {
        updateCartCount(count) {
            this.count = count
        }
    },
    mounted() {
        if (localStorage.getItem('cart')) {
            try {
                const cart = JSON.parse(localStorage.getItem('cart'));
                const cartCount = cart.reduce(function (a, b) {
                    return a + parseInt(b.quantity);
                }, 0);
                this.count = cartCount
            } catch (e) {
                console.log("Error retrieving cart from localStorage");
            }
        }
        this.$root.$on('updateCartCount', (count) => {
            this.count = count
        });
    }
};
