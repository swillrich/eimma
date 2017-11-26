update = (old, now) => {
  now["change"] = now["price"] - old["price"];
  now["changeRatio"] = now["change"] / old["price"];
  if (now["name"] != null) {
    now["name"] = old["name"];
  }
  return now;
};

calculateKO = product => {
  var barrier = product["barrier"];
  var underlying = product["underlying"];

  barrier["distance"] = barrier["price"] - underlying["price"];
  product["price"] = barrier["distance"] * product["multiplier"] * -1;
  product["leverage"] =
    barrier["price"] * product["multiplier"] / product["price"];

  return product;
};

exports.buildKO = (product, underlyingUpdate = null) => {
  var barrier = product["barrier"];
  var underlying = product["underlying"];

  calculateKO(product);

  if (underlyingUpdate != null) {
    var old = { price: product["price"], name: product["name"] };
    product["underlying"] = update(product["underlying"], underlyingUpdate);
    calculateKO(product);
    return update(old, product);
  }

  return product;
};
