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
    <p><a v-on:click="addToCart()" href="#" class="buy-now btn btn-sm height-auto px-4 py-3 btn-primary">Add To Cart</a></p>
  </div>
</template>

<script>
  module.exports = {
    name: 'shopping-cart-add',
    props: {
      'pid': String
    },
    data: function() {
      return {
          cart: [],
          id: this.pid,
          quantity: this.quantity
      };
    },
    methods: {
      incrementQuantity: function() {
        console.log("clicked incrementQuantity on: " + this);
        this.quantity++;
      },
      decrementQuantity: function() {
        console.log("clicked decrementQuantity on: " + this);
        if (this.quantity > 0) this.quantity--;
      },
      quantityTextEntered(e) {
        const inputVal = e.target.value;
        console.log("quantityTextEntered: " + inputVal);
        if (isNaN(inputVal)) alert("Please enter a valid number");
        else {
          this.quantity = parseInt(inputVal);
          // reset value
        }
      },
      addToCart() {
        console.log("clicked addToCart on: " + this);
        const newItem = { id: this.id, quantity: this.quantity };
        const index = this.cart.findIndex(x => x.id === this.id);
        if (index >= 0) {
          console.log("removing existing item for " + this.id + " from cart")
          this.cart.splice(index, 1);
        }
        this.cart.push(newItem);
        this.saveCart();
      },
      saveCart() {
        console.log("saving cart to local storage")
        const parsed = JSON.stringify(this.cart);
        localStorage.setItem('cart', parsed);
        this.updateCartCount()
      },
      updateCartCount() {
        const cartCount = this.cart.reduce(
            (sum, obj) => sum + parseInt(obj['quantity'])
            ,0
        );
        console.log("emitting 'updateCartCount' event for " + cartCount + " items")
        this.$root.$emit('updateCartCount', cartCount);
      }
    },
    mounted() {
      console.log("add-to-cart::mounted");
      console.log("retrieving cart from local storage");
      if (localStorage.getItem('cart')) {
        try {
          this.cart = JSON.parse(localStorage.getItem('cart'));
        } catch(e) {
          console.log("Error retrieving cart from localStorage");
          localStorage.removeItem('cart');
        }
      }
      this.quantity = 1;
    }
  };
</script>

<style scoped>
</style>
