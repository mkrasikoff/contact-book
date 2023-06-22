var selectedLogoId = null;

function selectLogo(id) {
    if (selectedLogoId) {
        var logo = document.getElementById('logo' + selectedLogoId);

        logo.classList.remove('selected');
    }

    document.getElementById('logo' + id).classList.add('selected');
    document.getElementById('logoId').value = id;
    selectedLogoId = id;
}

// Automatically select the logo if it's already set
var logoIdElement = document.getElementById('logoId');
var logoId = logoIdElement.value;

if (logoId) {
    selectLogo(logoId);
}
