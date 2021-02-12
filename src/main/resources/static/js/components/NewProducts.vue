<template>
  <div>
    <div class="site-section bg-light">
      <div class="container">
        <div class="row">
          <div class="title-section text-center col-12">
            <h2 class="text-uppercase">New Products</h2>
          </div>
        </div>
        <div class="row">
          <div v-for="product in products" :key="product.id" class="col-sm-6 col-lg-4 text-center item mb-4">
            <span v-if="product.onSale" class="tag">Sale</span>
            <a v-bind:href="'/products/' + product.id">
              <span v-if="product.image">
                <img v-bind:src="'/img/products/' + product.image" alt="Image" class="img-fluid">
              </span>
              <span v-else>
                <img v-bind:src="'/img/awaiting-image-sm.png'" alt="Image" class="img-fluid">
              </span>
            </a>
            <h3 class="text-dark"><a v-bind:href="'/products/' + product.id">{{  product.name }}</a></h3>
            <p v-if="product.onSale" class="price">
              <del>{{ currencySymbol }}{{  product.price }}</del> &mdash; {{ currencySymbol }}{{  product.salePrice }}
            </p>
            <p v-else class="price">
              {{ currencySymbol }}{{  product.price }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  module.exports = {
    name: 'new-products',
    props: {
      'currency': String
    },
    data: function() {
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
</script>

<style scoped>
</style>
