/*
 * modal.js
 *
 * A generic modal toggling script for Pico CSS.
 * Allows multiple modals to exist on the same page.
 * Any element can open/close a modal via:
 *   data-target="modalId"
 *   onclick="toggleModal(event)"
 */

// Config
const isOpenClass = "modal-is-open";
const openingClass = "modal-is-opening";
const closingClass = "modal-is-closing";
const scrollbarWidthCssVar = "--pico-scrollbar-width";
const animationDuration = 400; // ms
let visibleModal = null;

/*
 * toggleModal(event)
 *
 * Use this function as an inline onclick handler:
 *   <button data-target="deleteModal" onclick="toggleModal(event)">Delete</button>
 *
 * The script will find <dialog id="deleteModal">
 * and open/close it based on its current state.
 */
window.toggleModal = (event) => {
    event.preventDefault();
    const modalId = event.currentTarget.dataset.target;
    if (!modalId) return;
    const modal = document.getElementById(modalId);
    if (!modal) return;
    modal.open ? closeModal(modal) : openModal(modal);
};

/*
 * openModal(modal)
 *
 * Opens the specified modal <dialog>,
 * adding/removing appropriate Pico classes and handling scrollbars.
 */
function openModal(modal) {
    const { documentElement: html } = document;
    const scrollbarWidth = getScrollbarWidth();

    if (scrollbarWidth) {
        html.style.setProperty(scrollbarWidthCssVar, `${scrollbarWidth}px`);
    }
    html.classList.add(isOpenClass, openingClass);

    setTimeout(() => {
        visibleModal = modal;
        html.classList.remove(openingClass);
    }, animationDuration);

    modal.showModal();
}

/*
 * closeModal(modal)
 *
 * Closes the specified modal <dialog>.
 */
function closeModal(modal) {
    visibleModal = null;
    const { documentElement: html } = document;

    html.classList.add(closingClass);
    setTimeout(() => {
        html.classList.remove(closingClass, isOpenClass);
        html.style.removeProperty(scrollbarWidthCssVar);
        modal.close();
    }, animationDuration);
}

/*
 * Closing the modal by clicking outside the article
 * (if the user clicks outside the modal content, we close).
 */
document.addEventListener("click", (event) => {
    if (visibleModal === null) return;
    const modalContent = visibleModal.querySelector("article");
    if (!modalContent.contains(event.target)) {
        closeModal(visibleModal);
    }
});

/*
 * Close with Esc key
 */
document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && visibleModal) {
        closeModal(visibleModal);
    }
});

/*
 * Utility: Get scrollbar width
 */
function getScrollbarWidth() {
    return window.innerWidth - document.documentElement.clientWidth;
}
