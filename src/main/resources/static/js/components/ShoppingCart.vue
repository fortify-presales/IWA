<template>
  <div>
    <span v-if="cartIsEmpty">
       <div class="row">
        <div class="col-md-6 offset-3 text-center pb-5">
          <h4>Your shopping cart is empty!</h4>
          <div class="col-md-6 offset-3 pt-4">
            <button @click="continueShopping()" class="btn btn-outline-primary btn-md btn-block">Continue Shopping</button>
          </div>
        </div>
       </div>
    </span>
    <span v-else>
      <div class="row mb-5">
        <form class="col-md-12" method="post">
          <div class="site-blocks-table">
            <table class="table table-bordered">
              <thead>
              <tr>
                <th class="product-thumbnail">Image</th>
                <th class="product-name">Product</th>
                <th class="product-price">Price</th>
                <th class="product-quantity">Quantity</th>
                <th class="product-total">Total</th>
                <th class="product-remove">Remove</th>
              </tr>
              </thead>
              <tbody>

              <tr v-for="item in cart" :key="item.id">
                <td class="product-thumbnail">
                  <span v-if="item.image">
                    <img v-bind:src="'/img/products/' + item.image" alt="Image" class="img-fluid">
                  </span>
                  <span v-else>
                    <img v-bind:src="'/img/awaiting-image-sm.png'" alt="Image" class="img-fluid">
                  </span>
                </td>
                <td class="product-name">
                  <h2 class="h5 text-black">{{ item.name }}</h2>
                </td>
                <td>{{ currencySymbol }}{{ item.price }}</td>
                <td>
                  <div class="input-group mb-3" style="max-width: 120px;">
                    <div class="input-group-prepend">
                      <button class="btn btn-outline-primary js-btn-minus" v-on:click="decrementQuantity(item.id)" type="button">&minus;</button>
                    </div>
                    <input type="text" class="form-control text-center" :value="item.quantity" @input="quantityTextEntered($event, item.id)"
                           aria-label="Example text with button addon" aria-describedby="button-addon1">
                    <div class="input-group-append">
                      <button class="btn btn-outline-primary js-btn-plus" v-on:click="incrementQuantity(item.id)" type="button">&plus;</button>
                    </div>
                  </div>
                </td>
                <td>{{ currencySymbol }}{{ item.total }}</td>
                <td><a href="#" v-on:click="removeItem(item.id)" class="btn btn-primary height-auto btn-sm">X</a></td>
              </tr>
              </tbody>
            </table>
          </div>
        </form>
      </div>

      <div class="row">
        <div class="col-md-6">
          <div class="row mb-5">
            <div class="col-md-6 mb-3 mb-md-0">
              <button @click="updateCart()" class="btn btn-primary btn-md btn-block">Update Cart</button>
            </div>
            <div class="col-md-6">
              <button @click="continueShopping()" class="btn btn-outline-primary btn-md btn-block">Continue Shopping</button>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              <label class="text-black h4" for="coupon">Coupon</label>
              <p>Enter your coupon code if you have one.</p>
            </div>
            <div class="col-md-8 mb-3 mb-md-0">
              <input type="text" class="form-control py-3" id="coupon" placeholder="Coupon Code">
            </div>
            <div class="col-md-4">
              <button class="btn btn-primary btn-md px-4">Apply Coupon</button>
            </div>
          </div>
        </div>
        <div class="col-md-6 pl-5">
          <div class="row justify-content-end">
            <div class="col-md-7">
              <div class="row">
                <div class="col-md-12 text-right border-bottom mb-5">
                  <h3 class="text-black h4 text-uppercase">Cart Totals</h3>
                </div>
              </div>
              <div class="row mb-3">
                <div class="col-md-6">
                  <span class="text-black">Subtotal</span>
                </div>
                <div class="col-md-6 text-right">
                  <strong class="text-black">{{ currencySymbol }}{{ subTotal }}</strong>
                </div>
              </div>
              <div class="row mb-5">
                <div class="col-md-6">
                  <span class="text-black">Total</span>
                </div>
                <div class="col-md-6 text-right">
                  <strong class="text-black">{{ currencySymbol }}{{ total }}</strong>
                </div>
              </div>

              <div class="row">
                <div class="col-md-12">
                  <button @click="checkoutCart()" class="btn btn-primary btn-lg btn-block">Proceed To Checkout</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </span>
  </div>
</template>

<script>
  module.exports = {
    name: 'shopping-cart',
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
      let lsCart = [];
      if (localStorage.getItem('cart')) {
        try {
          lsCart = JSON.parse(localStorage.getItem('cart'));
        } catch(e) {
          console.log("shopping-cart::error retrieving cart from localStorage");
          localStorage.removeItem('cart');
        }
      }
      // get the latest product details from the database
      const size = Object.keys(lsCart).length;
      if (size > 0) {
        this.cartIsEmpty = false;
        lsCart.forEach(obj => {
          console.log("shopping-cart::retrieving latest product details for product id: " + obj.id);
          axios
              .get("/api/v3/products/" + obj.id)
              .then(response => {
                const responseItem = response.data;
                let price = responseItem.price;
                if (responseItem.onSale) {
                  price = responseItem.salePrice;
                }
                const newItem = {
                  id: obj.id,
                  quantity: obj.quantity,
                  image: responseItem.image,
                  name: responseItem.name,
                  price: price,
                  total: Number(price * (obj.quantity))

                };
                this.cart.total += newItem.total;
                this.cart.push(newItem);
              })
        })
      }
    },
    methods: {
      getItemQuantity: function(itemId) {
        let item = this.cart.find(x => x.id === itemId);
        return Number(item.quantity);
      },
      setItemQuantity: function(itemId, qty) {
        let item = this.cart.find(x => x.id === itemId);
        const index = this.cart.findIndex(x => x.id === itemId);
        item.quantity = qty;
        item.total = Number(item.quantity*item.price)
        Vue.set(this.cart, index, item);
      },
      incrementQuantity: function(itemId) {
        this.setItemQuantity(itemId, Number(this.getItemQuantity(itemId)+1))
      },
      decrementQuantity: function(itemId) {
        let qty = this.getItemQuantity(itemId);
        if (qty > 0) {
          this.setItemQuantity(itemId, Number(qty-1));
        }
      },
      quantityTextEntered: function(e, itemId) {
        const inputVal = e.target.value;
        if (isNaN(inputVal)) alert("Please enter a valid number");
        else {
          let qty = parseInt(inputVal);
          let item = this.cart.find(x => x.id === itemId);
          const index = this.cart.findIndex(x => x.id === itemId);
          if (qty >= 0) {
            this.setItemQuantity(itemId, qty);
          }
        }
      },
      removeItem(itemId) {
        const index = this.cart.findIndex(x => x.id === itemId);
        if (index >= 0) {
          console.log("shopping-cart::removing existing item '" + itemId + "' from cart")
          this.cart.splice(index, 1);
        }
        this.saveCart();
      },
      saveCart() {
        console.log("shopping-cart::saving cart to local storage");
        let sCart = [];
        let cartCount = 0;
        this.cart.forEach(obj => {
            const newItem = {
              id: obj.id,
              name: obj.name,
              price: obj.price,
              quantity: obj.quantity,
            };
            cartCount = cartCount + Number(obj.quantity);
            sCart.push(newItem);
        })
        // remove old cart
        localStorage.removeItem('cart');
        const parsed = JSON.stringify(sCart);
        localStorage.setItem('cart', parsed);
        // send update cart count event
        console.log("shopping-cart::emitting 'updateCartCount' event for " + cartCount + " items")
        this.$root.$emit('updateCartCount', cartCount);
      },
      updateCartCount() {
        const cartCount = this.cart.reduce((sum, obj) => sum + parseInt(obj['quantity']),0);
        console.log("shopping-cart::emitting 'updateCartCount' event for " + cartCount + " items")
        this.$root.$emit('updateCartCount', cartCount);
      },
      continueShopping() {
        window.location.href="/products";
      },
      updateCart() {
        // just reload for now
        location.reload();
        //this.$forceUpdate();
      },
      checkoutCart() {
        this.saveCart();
        window.location.href="/cart/checkout";
      }
    },
    computed: {

    },
    watch: {
      cart: function(val) {
        this.subTotal = this.cart.reduce((sum, item) => sum + Number(item.price*item.quantity), 0);
        // TODO: calculate any discounts
        this.total = this.subTotal;
        // update cart count
        this.updateCartCount();
      }
    },
    mounted() {

    }
  };
</script>

<style scoped>
</style>
