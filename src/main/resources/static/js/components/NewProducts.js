module.exports = {
    name: 'new-products',
    props: {
        'currency': String
    },
    data: function () {
        return {
            currencySymbol: '$',
            productsIsEmpty: true,
            products: [],
        };
    },
    async created() {


    },
    async mounted() {
        if (this.$props.currency) this.currencySymbol = this.$props.currency;
        axios
            .get("/api/v3/products", {
                params: {
                    limit: 3
                }
            }).then(response => {
            this.products = response.data;
            if (this.products.length > 0) this.productsIsEmpty = false;
        });
    }
};
