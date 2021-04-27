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
          <td class="text-black"><strong>Cart Subtotal</strong></td>
          <td class="text-black"><strong>{{ currencySymbol }}{{ subTotal }}</strong></td>
        </tr>
        <tr>
            <td class="text-black"><strong>Order Total</strong></td>
            <td class="text-black"><strong>{{ currencySymbol }}{{ total }}</strong></td>
        </tr>
        </tbody>
      </table>
    </span>
  </div>
</template>

<script src="/js/components/CartSummary.js"></script>

<style scoped>
</style>
