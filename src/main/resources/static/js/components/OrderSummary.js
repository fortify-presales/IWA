module.exports = {
    name: 'order-summary',
    props: {
        'cart': Object,
        'currency': String
    },
    data: function () {
        return {
            currencySymbol: '$',
            order: this.cart,
            orderIsEmpty: false,
        };
    },
    mounted() {
        try {
            this.order = JSON.parse(this.cart);
        } catch (e) {
            console.log("Error retrieving cart");
        }
        const size = Object.keys(this.order).length;
        if (size === 0) this.orderIsEmpty = true;
        if (this.$props.currency) this.currencySymbol = this.$props.currency;
    }
};
