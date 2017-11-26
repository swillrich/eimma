const pB = require("./common/product-builder");

var print = json => console.log(JSON.stringify(json, null, 2));

var koProduct = pB.buildKO({
  name: "WKN123456",
  isCall: true,
  multiplier: 1 / 100,
  barrier: {
    name: "DAX",
    price: "12900"
  },
  underlying: {
    name: "DAX",
    price: "13000"
  }
});

print(koProduct);

["13050", "13010"].forEach(p => {
  koProduct = pB.buildKO(koProduct, { price: p });
  print(koProduct);
});
