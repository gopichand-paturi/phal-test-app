project(
    'phal-test-app',
    'cpp',
    default_options: [
        'warning_level=3',
        'werror=true',
        'cpp_std=c++23',
        'buildtype=debugoptimized',
        'b_ndebug=if-release',
    ],
    license: 'Apache-2.0',
    version: '1.0',
    meson_version: '>=1.1.1',
)
add_project_arguments('-Wno-psabi', language: 'cpp')

if get_option('cpp_std') != 'c++23'
    error('This project requires c++23')
endif

if(get_option('buildtype') == 'minsize')
    add_project_arguments('-DNDEBUG', language : 'cpp')
endif

cxx = meson.get_compiler('cpp')

# Configuration header file(config.h) generation
conf_data = configuration_data()

conf_data.set_quoted('DEVTREE_EXPORT_FILTER_FILE', get_option('DEVTREE_EXPORT_FILTER_FILE'),
                      description : 'Path to the phal devtree export filter file'
                    )

conf_data.set_quoted('DEVTREE_EXP_FILE', get_option('DEVTREE_EXP_FILE'),
                      description : 'Path to the devtree export copy file'
                    )

conf_data.set_quoted('CEC_DEVTREE_RW_PATH', get_option('CEC_DEVTREE_RW_PATH'),
                      description : 'Path to the devtree file r/w version'
                    )

conf_data.set_quoted('CEC_DEVTREE_RO_BASE_PATH', get_option('CEC_DEVTREE_RO_BASE_PATH'),
                      description : 'Base path to the devtree file read only version'
                    )
conf_data.set_quoted('CEC_INFODB_PATH', get_option('CEC_INFODB_PATH'),
                      description : 'Path to the devtree attributes based database path'
                    )

conf_data.set_quoted('DEVTREE_REINIT_ATTRS_LIST', get_option('DEVTREE_REINIT_ATTRS_LIST'),
                      description : 'Path to the phal devtree reinit attribute list file'
                    )

conf_data.set_quoted('OP_DUMP_OBJ_PATH', get_option('op_dump_obj_path'),
                      description : 'Object path requesting OpenPOWER dumps'
                    )

configure_file(configuration : conf_data,
               output : 'config.h'
              )

unit_subs = configuration_data()
unit_subs.set('bindir', join_paths(get_option('prefix'), get_option('bindir')))
unit_subs.set('ENABLE_PHAL_TRUE', '')
unit_subs.set('CEC_DEVTREE_RW_PATH', get_option('CEC_DEVTREE_RW_PATH'),
               description : 'Path to the devtree file r/w version'
             )

sdbusplus_dep = dependency('sdbusplus')
sdeventplus_dep = dependency('sdeventplus')
pdi_dep = dependency('phosphor-dbus-interfaces')
phosphor_logging_dep = dependency('phosphor-logging')
cli11_dep = dependency('CLI11')

executable(
    'phal-test-app',
    [
        'src/phal_app.cpp',
        'src/phal_app_main.cpp',
    ],
    dependencies: [
        pdi_dep,
        phosphor_logging_dep,
        sdbusplus_dep,
        cxx.find_library('pdbg'),
        cxx.find_library('phal'),
        cxx.find_library('ekb'),
        cxx.find_library('ipl'),
        cxx.find_library('dtree'),
        dependency('libdt-api'),
        dependency('threads'),
        dependency('fmt'),
    ],
    install: true
)

unit_files = [
    'service_files/phal-test-app@.service',
]

systemd_system_unit_dir = dependency('systemd').get_variable(
    'systemdsystemunitdir'
    pkgconfig_define: ['prefix', get_option('prefix')])
foreach u : unit_files
    configure_file(
        configuration: unit_subs,
        input: u + '.in',
        install: true,
        install_dir: systemd_system_unit_dir,
        output: '@BASENAME@'
    )
endforeach
