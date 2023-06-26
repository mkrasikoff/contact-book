var selectedLogoId = null;

function selectLogo(id) {
    if (selectedLogoId) {
        var logo = document.getElementById('logo' + selectedLogoId);

        logo.classList.remove('selected');
    }

    var selectedLogo = document.getElementById('logo' + id);
    selectedLogo.classList.add('selected');
    document.getElementById('logoId').value = id;
    selectedLogoId = id;

    // Get the src of the selected logo and set it to the selectedLogo image.
    var selectedLogoSrc = selectedLogo.getAttribute('src');
    document.getElementById('selectedLogo').src = selectedLogoSrc;
}

// Automatically select the logo if it's already set
var logoIdElement = document.getElementById('logoId');
var logoId = logoIdElement.value;

if (logoId) {
    selectLogo(logoId);
} else {
    // Select the first logo if none is selected
    selectLogo(1);
}
