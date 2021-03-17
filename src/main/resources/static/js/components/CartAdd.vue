<template>
  <div class="add-to-cart">
    <div class="mb-5">
      <div class="input-group mb-3" style="max-width: 220px;">
        <div class="input-group-prepend">
          <button id="quantity-minus" v-on:click="decrementQuantity()" class="btn btn-outline-primary js-btn-minus" type="button">&minus;</button>
        </div>
        <input type="text" class="form-control text-center" :value="quantity" @input="quantityTextEntered">
        <div class="input-group-append">
          <button id="quantity-plus" v-on:click="incrementQuantity()" class="btn btn-outline-primary js-btn-plus" type="button">&plus;</button>
        </div>
      </div>
    </div>
    <p><a v-on:click="addToCart()" :disabled="!instock" href="#" class="buy-now btn btn-sm height-auto px-4 py-3 btn-primary">Add To Cart</a></p>
  </div>
</template>

<script src="./CartAdd.js"></script>

<style scoped>
</style>
