<template>
  <div>
    <span v-if="cartIsEmpty">
       <div class="row">
        <div class="offset-2 text-center pb-5">
          <h5>Your shopping cart is empty!</h5>
          <div class="offset-1 pt-4">
            <button @click="continueShopping()" class="btn btn-outline-primary btn-md btn-block">Continue Shopping</button>
          </div>
        </div>
       </div>
    </span>
    <span v-else>
      <table class="table site-block-order-table mb-5">
        <thead>
        <th>Product</th>
        <th>Total</th>
        </thead>
        <tbody>
          <tr v-for="item in cart" :key="item.id">
            <td>{{ item.name }} <strong class="mx-2">x</strong> {{ item.quantity }}</td>
            <td>{{ currencySymbol }}{{ item.price }}</td>
          </tr>
        <tr>
            <td class="text-black font-weight-bold"><strong>Cart Subtotal</strong></td>
            <td class="text-black">{{ currencySymbol }}{{ subTotal }}</td>
        </tr>
        <tr>
            <td class="text-black font-weight-bold"><strong>Order Total</strong></td>
            <td class="text-black font-weight-bold"><strong>{{ currencySymbol }}{{ total }}</strong></td>
        </tr>
        </tbody>
      </table>
    </span>
  </div>
</template>

<script>
  module.exports = {
    name: 'shopping-cart-summary',
    data: function() {
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
        } catch(e) {
          console.log("cart-summary::error retrieving cart from localStorage");
          localStorage.removeItem('cart');
        }
      }
      const size = Object.keys(this.cart).length;
      if (size > 0) { this.cartIsEmpty = false; }
    },
    methods: {
      continueShopping() {
        window.location.href="/products";
      },
      updateCart() {
        // just reload for now
        location.reload();
        //this.$forceUpdate();
      },
      placeOrder() {
        window.location.href="/cart/confirm";
      }
    },
    computed: {

    },
    watch: {
      cart: function(val) {
        this.subTotal = this.cart.reduce((sum, item) => sum + Number(item.price*item.quantity), 0);
        // TODO: calculate any discounts
        this.total = this.subTotal;
      }
    },
    mounted() {

    }
  };
</script>

<style scoped>
</style>
