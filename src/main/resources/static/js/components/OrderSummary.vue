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

<script src="OrderSummary.js"></script>

<style scoped>
</style>
