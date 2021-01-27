<template>
  <div>
    <span v-if="orderIsEmpty">
       <div class="row">
        <div class="offset-2 text-center pb-5">
          <h5>Your order was is empty!</h5>
        </div>
       </div>
    </span>
    <span v-else>
      <table class="table site-block-order-table">
        <!--thead>
        <th>Product</th>
        <th>Total</th>
        </thead-->
        <tbody>
          <tr v-for="item in order" :key="item.id">
            <td>{{ item.name }} <strong class="mx-2">x</strong> {{ item.quantity }}</td>
            <td>{{ currencySymbol }}{{ item.price }}</td>
          </tr>
        </tbody>
      </table>
    </span>
  </div>
</template>

<script>
  module.exports = {
    name: 'order-summary',
    props: {
      'cart': Object,
      'currency': String
    },
    data: function() {
      return {
        currencySymbol: '$',
        order: this.cart,
        orderIsEmpty: false,
      };
    },
    mounted() {
      try {
        this.order = JSON.parse(this.cart);
      } catch(e) {
        console.log("Error retrieving cart");
      }
      const size = Object.keys(this.order).length;
      if (size === 0) this.orderIsEmpty = true;
      if (this.$props.currency) this.currencySymbol = this.$props.currency;
    }
  };
</script>

<style scoped>
</style>
