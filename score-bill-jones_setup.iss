; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "ScoreBillJones"
#define MyAppVersion "3.2"
#define MyAppPublisher "Alan Curran"
#define MyAppExeName "ScoreBillJones.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={0DC236B3-FCB2-4D11-8761-EF344ED25818}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\{#MyAppName}
DisableProgramGroupPage=yes
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
OutputDir=D:\Users\arcur\IntelliJProjects\ScoreBillJones\scorebj.app\target\install
OutputBaseFilename=tablifi_setup-{#MyAppVersion}
SetupIconFile=D:\Users\arcur\IntelliJProjects\ScoreBillJones\scorebj.app\src\main\resources\Club.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern
Uninstallable=yes
UninstallDisplayIcon=D:\Users\arcur\IntelliJProjects\Tablifi\scorebj.app\src\main\resources\Club.ico
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "D:\Users\arcur\IntelliJProjects\ScoreBillJones\scorebj.app\target\scorebj.app-dist\ScoreBillJones\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Users\arcur\IntelliJProjects\ScoreBillJones\scorebj.app\target\scorebj.app-dist\ScoreBillJones\jre"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

