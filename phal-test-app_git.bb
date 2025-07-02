SUMMARY = "phal test application"
DESCRIPTION = "Framework to independently test phal code"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PR = "r1"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit meson obmc-phosphor-utils pkgconfig
inherit systemd

SRC_URI = "git://github.com/gopichand-paturi/phal-test-app;nobranch=1;protocol=https"
SRCREV = "30a83362c3b3f9c408330654a4066dd24d382b02"

DEPENDS += " \
            phosphor-logging \ 
            phosphor-dbus-interfaces \
            pdbg \
            "
EXTRA_OEMESON += "-Dop_dump_obj_path=/xyz/openbmc_project/dump/system"

PACKAGECONFIG ??= "${@bb.utils.filter('MACHINE_FEATURES', 'phal op-fsi', d)}"
PACKAGECONFIG[phal] = "-Dphal=enabled, -Dphal=disabled -Dp9=enabled, ipl pdata libekb"
PACKAGECONFIG[op-fsi] = "-Dopenfsi=enabled, -Dopenfsi=disabled"
PACKAGECONFIG = " op-fsi"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} +=  " phal-test-app@.service"

ALLOW_EMPTY:${PN} = "1"

pkg_postinst:${PN}() {
        LINK="$D$systemd_system_unitdir/multi-user.target.wants/phal-test-app@0.service"
		TARGET="../phal-test-app@.service"
		ln -s $TARGET $LINK
}
pkg_prerm:${PN}() {
        LINK="$D$systemd_system_unitdir/multi-user.target.wants/phal-test-app@0.service"
        rm $LINK
}
