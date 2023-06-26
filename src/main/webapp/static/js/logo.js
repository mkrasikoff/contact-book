// This variable keeps track of the id of the currently selected logo.
var selectedLogoId = null;

/**
 * Selects a logo.
 *
 * @param {number} id - The id of the logo to select.
 */
function selectLogo(id) {
    // If a logo is already selected, remove the 'selected' class from it.
    deselectCurrentLogo();

    // Select the new logo.
    selectNewLogo(id);
}

/**
 * If a logo is already selected, this function removes the 'selected' class from it.
 */
function deselectCurrentLogo() {
    if (selectedLogoId) {
        var logo = document.getElementById('logo' + selectedLogoId);
        logo.classList.remove('selected');
    }
}

/**
 * Selects a new logo.
 *
 * @param {number} id - The id of the logo to select.
 */
function selectNewLogo(id) {
    var selectedLogo = document.getElementById('logo' + id);

    // Add the 'selected' class to the new logo.
    selectedLogo.classList.add('selected');

    // Update the logoId field in the form.
    document.getElementById('logoId').value = id;

    // Update the selectedLogoId variable.
    selectedLogoId = id;

    // Update the src of the selectedLogo image.
    updateSelectedLogoSrc(selectedLogo);
}

/**
 * Updates the src of the selectedLogo image.
 *
 * @param {HTMLImageElement} selectedLogo - The selected logo.
 */
function updateSelectedLogoSrc(selectedLogo) {
    var selectedLogoSrc = selectedLogo.getAttribute('src');
    document.getElementById('selectedLogo').src = selectedLogoSrc;
}

// If a logo is already selected, select it. Otherwise, select the first logo.
(function autoSelectLogo() {
    var logoIdElement = document.getElementById('logoId');
    var logoId = logoIdElement.value;

    if (logoId) {
        selectLogo(logoId);
    } else {
        // Select the first logo if none is selected
        selectLogo(1);
    }
})();
