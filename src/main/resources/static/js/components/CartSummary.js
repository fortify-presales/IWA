module.exports = {
    name: 'shopping-cart-summary',
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
        if (localStorage.getItem('cart')) {
            try {
                this.cart = JSON.parse(localStorage.getItem('cart'));
            } catch (e) {
                console.log("Error retrieving cart from localStorage");
                localStorage.removeItem('cart');
            }
        }
        const size = Object.keys(this.cart).length;
        if (size > 0) {
            this.cartIsEmpty = false;
            this.subTotal = this.cart.reduce((sum, item) => sum + Number(item.price * item.quantity), 0);
            // TODO: calculate any discounts
            this.total = this.subTotal;
            document.getElementById("jsonCart").value = localStorage.getItem('cart');
            document.getElementById("orderAmount").value = this.total;

        }
    },
    methods: {
        updateCart() {
            // just reload for now
            location.reload();
            //this.$forceUpdate();
        },
        continueShopping() {

        }
    },
    computed: {},
    watch: {
        cart: function (val) {
            this.subTotal = this.cart.reduce((sum, item) => sum + Number(item.price * item.quantity), 0);
            // TODO: calculate any discounts
            this.total = this.subTotal;
        }
    },
    mounted() {
        if (this.$props.currency) this.currencySymbol = this.$props.currency;
    }
};
