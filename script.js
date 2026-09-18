const products = readProductsFromTemplate();

const money = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL"
});

const state = {
  category: "Todos",
  query: "",
  sort: "featured",
  cart: loadCart()
};

const productGrid = document.querySelector("#productGrid");
const emptyState = document.querySelector("#emptyState");
const categoryFilters = document.querySelector("#categoryFilters");
const productSearch = document.querySelector("#productSearch");
const sortProducts = document.querySelector("#sortProducts");
const cartDrawer = document.querySelector("#cartDrawer");
const openCartButton = document.querySelector("#openCart");
const closeCartButton = document.querySelector("#closeCart");
const cartBackdrop = document.querySelector("#cartBackdrop");
const cartItems = document.querySelector("#cartItems");
const cartCount = document.querySelector("#cartCount");
const cartSubtotal = document.querySelector("#cartSubtotal");
const cartShipping = document.querySelector("#cartShipping");
const cartTotal = document.querySelector("#cartTotal");
const checkoutButton = document.querySelector("#checkoutButton");
const newsletterForm = document.querySelector("#newsletterForm");
const toast = document.querySelector("#toast");
const titleTimeline = document.querySelector("#titleTimeline");
const titleTimelineProgress = document.querySelector("#titleTimelineProgress");

class OfferCountdown {
  constructor(selector, hours = 18) {
    this.root = document.querySelector(selector);
    this.duration = hours * 60 * 60 * 1000;
    this.storageKey = "flastore-offer-deadline";
    this.deadline = this.getDeadline();
    this.timer = null;
  }

  getDeadline() {
    const savedDeadline = Number(localStorage.getItem(this.storageKey));
    const now = Date.now();

    if (savedDeadline && savedDeadline > now) {
      return savedDeadline;
    }

    const freshDeadline = now + this.duration;
    localStorage.setItem(this.storageKey, String(freshDeadline));
    return freshDeadline;
  }

  start() {
    if (!this.root) {
      return;
    }

    this.update();
    this.timer = window.setInterval(() => this.update(), 1000);
  }

  update() {
    const remaining = Math.max(this.deadline - Date.now(), 0);
    const hours = Math.floor(remaining / 1000 / 60 / 60);
    const minutes = Math.floor((remaining / 1000 / 60) % 60);
    const seconds = Math.floor((remaining / 1000) % 60);

    this.setUnit("hours", hours);
    this.setUnit("minutes", minutes);
    this.setUnit("seconds", seconds);

    const percent = Math.max((remaining / this.duration) * 100, 0);
    const bar = this.root.querySelector("[data-countdown-bar]");

    if (bar) {
      bar.style.width = `${percent}%`;
    }

    if (remaining === 0) {
      window.clearInterval(this.timer);
      localStorage.removeItem(this.storageKey);
    }
  }

  setUnit(unit, value) {
    const element = this.root.querySelector(`[data-countdown-unit="${unit}"]`);

    if (element) {
      element.textContent = String(value).padStart(2, "0");
    }
  }
}

function readProductsFromTemplate() {
  const productsData = document.querySelector("#productsData");

  if (productsData?.textContent.trim()) {
    try {
      const parsedProducts = JSON.parse(productsData.textContent);
      return parsedProducts.length ? parsedProducts : readProductsFromCards();
    } catch {
      return readProductsFromCards();
    }
  }

  return readProductsFromCards();
}

function readProductsFromCards() {
  return Array.from(document.querySelectorAll(".product-card")).map((card) => ({
    id: card.dataset.id || card.querySelector("[data-add]")?.dataset.add,
    name: card.dataset.name || card.querySelector("h3")?.textContent.trim(),
    category: card.dataset.category || card.querySelector(".product-kicker")?.textContent.trim(),
    description: card.dataset.description || card.querySelector("p")?.textContent.trim(),
    price: Number(card.dataset.price || 0),
    featured: Number(card.dataset.featured || 0)
  }));
}

function loadCart() {
  try {
    return JSON.parse(localStorage.getItem("flastore-cart")) || {};
  } catch {
    return {};
  }
}

function saveCart() {
  localStorage.setItem("flastore-cart", JSON.stringify(state.cart));
}

function getProductById(id) {
  return products.find((product) => product.id === id);
}

function getProductCards() {
  return Array.from(productGrid.querySelectorAll(".product-card"));
}

function renderCategories() {
  const categories = ["Todos", ...new Set(products.map((product) => product.category))];

  categoryFilters.innerHTML = categories
    .map(
      (category) => `
        <button
          class="${category === state.category ? "is-active" : ""}"
          type="button"
          data-category="${category}"
        >
          ${category}
        </button>
      `
    )
    .join("");
}

function applyProductFilters() {
  const query = state.query.trim().toLowerCase();
  const sortedCards = getProductCards().sort((first, second) => compareProductCards(first, second));
  let visibleCount = 0;

  sortedCards.forEach((card) => {
    productGrid.appendChild(card);

    const text = `${card.dataset.name} ${card.dataset.description} ${card.dataset.category}`.toLowerCase();
    const matchesCategory = state.category === "Todos" || card.dataset.category === state.category;
    const matchesQuery = text.includes(query);
    const shouldShow = matchesCategory && matchesQuery;

    card.hidden = !shouldShow;
    visibleCount += shouldShow ? 1 : 0;
  });

  emptyState.hidden = visibleCount > 0;
}

function compareProductCards(first, second) {
  if (state.sort === "price-asc") {
    return Number(first.dataset.price) - Number(second.dataset.price);
  }

  if (state.sort === "price-desc") {
    return Number(second.dataset.price) - Number(first.dataset.price);
  }

  if (state.sort === "name") {
    return first.dataset.name.localeCompare(second.dataset.name, "pt-BR");
  }

  return Number(first.dataset.featured) - Number(second.dataset.featured);
}

function initScrollAnimations() {
  const revealItems = document.querySelectorAll(".scroll-reveal");

  if (!revealItems.length) {
    return;
  }

  if (!("IntersectionObserver" in window)) {
    revealItems.forEach((item) => item.classList.add("is-visible"));
    return;
  }

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        }
      });
    },
    {
      rootMargin: "0px 0px -90px 0px",
      threshold: 0.22
    }
  );

  revealItems.forEach((item) => observer.observe(item));
}

function updateTitleTimelineProgress() {
  if (!titleTimeline || !titleTimelineProgress) {
    return;
  }

  const rect = titleTimeline.getBoundingClientRect();
  const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
  const startPoint = viewportHeight * 0.78;
  const travelDistance = rect.height + viewportHeight * 0.16;
  const progress = Math.min(Math.max((startPoint - rect.top) / travelDistance, 0), 1);

  titleTimelineProgress.style.height = `${progress * 100}%`;
}

function addToCart(id) {
  const product = getProductById(id);

  if (!product) {
    showToast("Produto não encontrado.");
    return;
  }

  state.cart[id] = (state.cart[id] || 0) + 1;
  saveCart();
  renderCart();
  showToast(`${product.name} adicionado ao carrinho.`);
}

function changeQuantity(id, amount) {
  const currentQuantity = state.cart[id] || 0;
  const nextQuantity = currentQuantity + amount;

  if (nextQuantity <= 0) {
    delete state.cart[id];
  } else {
    state.cart[id] = nextQuantity;
  }

  saveCart();
  renderCart();
}

function removeFromCart(id) {
  const product = getProductById(id);
  delete state.cart[id];
  saveCart();
  renderCart();
  showToast(`${product?.name || "Produto"} removido.`);
}

function getCartLines() {
  return Object.entries(state.cart)
    .map(([id, quantity]) => {
      const product = getProductById(id);
      return product ? { ...product, quantity } : null;
    })
    .filter(Boolean);
}

function getCartTotals() {
  const lines = getCartLines();
  const subtotal = lines.reduce((sum, item) => sum + Number(item.price) * item.quantity, 0);
  const shipping = subtotal > 0 ? 19 : 0;

  return {
    lines,
    subtotal,
    shipping,
    total: subtotal + shipping,
    quantity: lines.reduce((sum, item) => sum + item.quantity, 0)
  };
}

function renderCart() {
  const totals = getCartTotals();
  cartCount.textContent = String(totals.quantity);
  cartSubtotal.textContent = money.format(totals.subtotal);
  cartShipping.textContent = money.format(totals.shipping);
  cartTotal.textContent = money.format(totals.total);
  checkoutButton.disabled = totals.quantity === 0;

  if (!totals.lines.length) {
    cartItems.innerHTML = '<p class="cart-empty">Seu carrinho está vazio.</p>';
    return;
  }

  cartItems.innerHTML = totals.lines
    .map(
      (item) => `
        <article class="cart-item">
          <div>
            <h3>${item.name}</h3>
            <p>${money.format(Number(item.price))} cada</p>
          </div>
          <div class="quantity-control" aria-label="Quantidade de ${item.name}">
            <button class="quantity-button" type="button" data-decrease="${item.id}" aria-label="Diminuir quantidade">
              &minus;
            </button>
            <span>${item.quantity}</span>
            <button class="quantity-button" type="button" data-increase="${item.id}" aria-label="Aumentar quantidade">
              +
            </button>
          </div>
          <button class="remove-button" type="button" data-remove="${item.id}">Remover</button>
        </article>
      `
    )
    .join("");
}

function openCart() {
  cartDrawer.classList.add("is-open");
  cartDrawer.setAttribute("aria-hidden", "false");
  openCartButton.setAttribute("aria-expanded", "true");
  document.body.classList.add("cart-open");
  closeCartButton.focus();
}

function closeCart() {
  cartDrawer.classList.remove("is-open");
  cartDrawer.setAttribute("aria-hidden", "true");
  openCartButton.setAttribute("aria-expanded", "false");
  document.body.classList.remove("cart-open");
  openCartButton.focus();
}

let toastTimer = null;

function showToast(message) {
  toast.textContent = message;
  toast.classList.add("is-visible");
  window.clearTimeout(toastTimer);
  toastTimer = window.setTimeout(() => {
    toast.classList.remove("is-visible");
  }, 2600);
}

function finishCheckout() {
  const totals = getCartTotals();

  if (!totals.lines.length) {
    showToast("Adicione um produto antes de finalizar.");
    return;
  }

  const orderCode = `FLA-${Math.floor(1000 + Math.random() * 9000)}`;
  state.cart = {};
  saveCart();
  renderCart();
  closeCart();
  showToast(`Pedido ${orderCode} finalizado. Total: ${money.format(totals.total)}.`);
}

categoryFilters.addEventListener("click", (event) => {
  const button = event.target.closest("[data-category]");

  if (!button) {
    return;
  }

  state.category = button.dataset.category;
  renderCategories();
  applyProductFilters();
});

productSearch.addEventListener("input", (event) => {
  state.query = event.target.value;
  applyProductFilters();
});

sortProducts.addEventListener("change", (event) => {
  state.sort = event.target.value;
  applyProductFilters();
});

productGrid.addEventListener("click", (event) => {
  const addButton = event.target.closest("[data-add]");

  if (addButton) {
    addToCart(addButton.dataset.add);
  }
});

cartItems.addEventListener("click", (event) => {
  const increaseButton = event.target.closest("[data-increase]");
  const decreaseButton = event.target.closest("[data-decrease]");
  const removeButton = event.target.closest("[data-remove]");

  if (increaseButton) {
    changeQuantity(increaseButton.dataset.increase, 1);
  }

  if (decreaseButton) {
    changeQuantity(decreaseButton.dataset.decrease, -1);
  }

  if (removeButton) {
    removeFromCart(removeButton.dataset.remove);
  }
});

openCartButton.addEventListener("click", openCart);
closeCartButton.addEventListener("click", closeCart);
cartBackdrop.addEventListener("click", closeCart);
checkoutButton.addEventListener("click", finishCheckout);

newsletterForm.addEventListener("submit", (event) => {
  event.preventDefault();
  const formData = new FormData(newsletterForm);
  const name = String(formData.get("name")).trim();
  showToast(`${name}, cadastro realizado com sucesso.`);
  newsletterForm.reset();
});

document.addEventListener("keydown", (event) => {
  if (event.key === "Escape" && cartDrawer.classList.contains("is-open")) {
    closeCart();
  }
});

window.addEventListener("scroll", updateTitleTimelineProgress, { passive: true });
window.addEventListener("resize", updateTitleTimelineProgress);

renderCategories();
applyProductFilters();
renderCart();
initScrollAnimations();
updateTitleTimelineProgress();
new OfferCountdown("[data-offer-countdown]").start();
