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

<script src="./ShoppingCart.js"></script>

<style scoped>
</style>
