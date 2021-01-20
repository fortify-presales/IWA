<template>
  <span id="shopping-cart-count" class="number">{{ count }}</span>
</template>

<script>
  module.exports = {
    name: 'shopping-cart-count',
    data: function() {
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
      console.log("shopping-cart-count::mounted");
      console.log("retrieving cart from local storage");
      if (localStorage.getItem('cart')) {
        try {
          const cart = JSON.parse(localStorage.getItem('cart'));
          const cartCount = cart.reduce(function(a, b) {
            return a + parseInt(b.quantity);
          }, 0);
          console.log("setting cart count to: " + cartCount)
          this.count = cartCount
        } catch(e) {
          console.log("Error retrieving cart from localStorage");
        }
      }
      this.$root.$on('updateCartCount', (count) => {
        console.log("received updated cart cart of: " + count)
        this.count = count
      });
    }
  };
</script>

<style scoped>
</style>
