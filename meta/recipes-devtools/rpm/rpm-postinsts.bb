DESCRIPTION = "RPM postinstall script"
SECTION = "core"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780"

RDEPENDS_${PN} = "base-files"

inherit allarch
#
# Allow distributions to alter when [postponed] package install scripts are run
#
POSTINSTALL_INITPOSITION ?= "98"

POSTLOG ?= "/var/log/postinstall.log"
REDIRECT_CMD = "${@base_contains('IMAGE_FEATURES', 'debug-tweaks', '>>${POSTLOG} 2>&1', '', d)}"

do_fetch() {
	:
}

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -d ${D}/${sysconfdir}/rcS.d
	# Stop $i getting expanded below...
	i=\$i
	cat > ${D}${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts << EOF
#!/bin/sh

. /etc/default/rcS

[ -d /etc/rpm-postinsts ] && for i in \`ls /etc/rpm-postinsts/ \`; do
	i=/etc/rpm-postinsts/$i
	echo "Running postinst $i..."
	if [ -f $i ] && $i ${REDIRECT_CMD}; then
		rm $i
	else
		echo "ERROR: postinst $i failed."
	fi
done
rm -f ${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts 2>/dev/null
EOF
	chmod 0755 ${D}${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}run-postinsts
}
