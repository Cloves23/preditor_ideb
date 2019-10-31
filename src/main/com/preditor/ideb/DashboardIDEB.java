package com.preditor.ideb;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class DashboardIDEB extends Application {
	private static int MIN_HEIGHT = 290;
	private static int MIN_WIDTH = 700;
	
	private Stage primaryStage;
	private VBox rootBox;
	
	private File csv;
	private TextField fileLocationField;
	private TextField instanceField;
	
	private TextField abJ48Field;
	private TextField blJ48Field;
	private TextField abJRipField;
	private TextField blJRipField;
	private TextField abRepTreeField;
	private TextField blRepTreeField;
	
	private TextField abOneRField;
	private TextField blOneRField;
	private TextField abLibSVMField;
	private TextField blLibSVMField;
	private TextField abIBKField;
	private TextField blIBKField;
	
	private Preditor preditor;
	
	private void statusGrid() {
		GridPane grid = new GridPane();
		
		grid.setPrefWidth(MIN_WIDTH);
		grid.setPadding(new Insets(10));
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(5);
		grid.setVgap(5);
		
		ColumnConstraints colConstraints = new ColumnConstraints();
		colConstraints.setHalignment(HPos.RIGHT);
		colConstraints.setMinWidth(150);
		grid.getColumnConstraints().add(colConstraints);
		
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setValignment(VPos.CENTER);
		grid.getRowConstraints().add(rowConstraints);
		
		grid.add(newLabel("Classificadores:", true, 16), 0, 0);
		
		GridPane gridControls = new GridPane();
		gridControls.setHgap(5);
		gridControls.setVgap(5);

		double width = 120;
		colConstraints = new ColumnConstraints();
		colConstraints.setHalignment(HPos.RIGHT);
		colConstraints.setMinWidth(width);
		colConstraints.setMaxWidth(width);
		colConstraints.setPrefWidth(width);
		gridControls.getColumnConstraints().add(colConstraints);
		
		for (int i = 0; i <= 3; i++) {
			colConstraints = new ColumnConstraints();
			colConstraints.setHalignment(HPos.CENTER);
			colConstraints.setMinWidth(width);
			colConstraints.setMaxWidth(width);
			colConstraints.setPrefWidth(width);
			gridControls.getColumnConstraints().add(colConstraints);
		}
		
		gridControls.add(newLabel("J48"), 1, 0);
		gridControls.add(newLabel("JRip"), 2, 0);
		gridControls.add(newLabel("REP Tree"), 3, 0);
		gridControls.add(newLabel("Acima da Média:"), 0, 1);
		gridControls.add(newLabel("Abaixo da Média:"), 0, 2);
		gridControls.add(abJ48Field, 1, 1);
		gridControls.add(blJ48Field, 1, 2);
		gridControls.add(abJRipField, 2, 1);
		gridControls.add(blJRipField, 2, 2);
		gridControls.add(abRepTreeField, 3, 1);
		gridControls.add(blRepTreeField, 3, 2);
		
		gridControls.add(newLabel("OneR"), 1, 3);
		gridControls.add(newLabel("LibSVM"), 2, 3);
		gridControls.add(newLabel("IBK"), 3, 3);
		gridControls.add(newLabel("Acima da Média:"), 0, 4);
		gridControls.add(newLabel("Abaixo da Média:"), 0, 5);
		gridControls.add(abOneRField, 1, 4);
		gridControls.add(blOneRField, 1, 5);
		gridControls.add(abLibSVMField, 2, 4);
		gridControls.add(blLibSVMField, 2, 5);
		gridControls.add(abIBKField, 3, 4);
		gridControls.add(blIBKField, 3, 5);
		
		grid.add(gridControls, 1, 0);
		
		this.rootBox.getChildren().add(0, grid);
	}
	
	private void dataGrid() {
		GridPane grid = new GridPane();

		grid.setPrefWidth(MIN_WIDTH);
		grid.setPadding(new Insets(10));
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(5);
		grid.setVgap(5);
		
		double width = 190;
		ColumnConstraints colConstraints = new ColumnConstraints();
		colConstraints.setHalignment(HPos.RIGHT);
		colConstraints.setMinWidth(width);
		colConstraints.setMaxWidth(width);
		colConstraints.setPrefWidth(width);
		grid.getColumnConstraints().add(colConstraints);

		colConstraints = new ColumnConstraints();
		colConstraints.setHgrow(Priority.SOMETIMES);
		colConstraints.setMaxWidth(MIN_WIDTH - width);
		grid.getColumnConstraints().add(colConstraints);

		HBox hBox = new HBox(5);
		Button btnClassifty = classifyStrButton();
		grid.add(newLabel("Classificar Instância:"), 0, 1);
		HBox.setHgrow(instanceField, Priority.ALWAYS);
		hBox.getChildren().addAll(instanceField, btnClassifty);
		grid.add(hBox, 1, 1);
		
		Button btnOpenFile = getFileButton();
		btnClassifty = classifyFileButton();

		hBox = new HBox(5);
		HBox.setHgrow(fileLocationField, Priority.ALWAYS);
		grid.add(newLabel("Classificar a partir de planilha:"), 0, 2);
		hBox.getChildren().addAll(fileLocationField, btnOpenFile, btnClassifty);
		grid.add(hBox, 1, 2);
		
		this.rootBox.getChildren().add(1, grid);
	}

	private TextField readOnlyField() {
		TextField field = new TextField();
		field.setEditable(false);
		return field;
	}
	
	private Label newLabel(String text) {
		return newLabel(text, false, 14);
	}
	
	private Label newLabel(String text, boolean bold, double size) {
		Label label = new Label(text);
		if (bold) {
			label.setFont(Font.font("Arial", FontWeight.BOLD, size));
		} else {
			label.setFont(Font.font("Arial", size));
		}
		return label;
	}
	
	private Button getFileButton() {
		Button button = new Button("Abrir CSV");
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Selecione um CSV");
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo CSV", "*.csv"));
				csv = fileChooser.showOpenDialog(primaryStage);
				if (csv != null) {
					fileLocationField.setText(csv.getAbsolutePath());
				}
			}
		});
		return button;
	}
	
	private Button classifyStrButton() {
		Button button = new Button("Classificar");
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (instanceField.getText() == null || instanceField.getText().isEmpty()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Classificar Instância");
					alert.setHeaderText("Dados não informados");
					alert.setContentText("Por favor, insira os valores da instância.");
					alert.showAndWait();
				} else {
					try {
						cleanValues();
						addValues(preditor.classifyAll(instanceField.getText()));
					} catch (Exception e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erro durante o processamento");
						alert.setContentText("Não foi possível classificar os dados. Verifique-os e tente novamente.");
						alert.showAndWait();
					}
				}
			}
		});
		return button;
	}
	
	private Button classifyFileButton() {
		Button button = new Button("Classificar");
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (csv == null) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Seleção do Arquivo");
					alert.setHeaderText("CSV não informado");
					alert.setContentText("Por favor, procure e selecione o CSV desejado para classificação.");
					alert.showAndWait();
				} else {
					try {
						cleanValues();
						addValues(preditor.classifyAll(csv));
					} catch (Exception e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Erro durante o processamento");
						alert.setContentText("Não foi possível classificar os dados. Verifique-os e tente novamente.");
						alert.showAndWait();
					}
				}
			}
		});
		return button;
	}
	
	private void addValues(Preditor.Averages avg) throws Exception {
		abJ48Field.setText(avg.formatedAbove(ClassifierType.J48));
		blJ48Field.setText(avg.formatedBelow(ClassifierType.J48));
		
		abJRipField.setText(avg.formatedAbove(ClassifierType.J_RIP));
		blJRipField.setText(avg.formatedBelow(ClassifierType.J_RIP));

		// TODO: ???
		abRepTreeField.setText(avg.formatedAbove(ClassifierType.J48));
		blRepTreeField.setText(avg.formatedBelow(ClassifierType.J48));

		abOneRField.setText(avg.formatedAbove(ClassifierType.ONE_R));
		blOneRField.setText(avg.formatedBelow(ClassifierType.ONE_R));

		abLibSVMField.setText(avg.formatedAbove(ClassifierType.SVM));
		blLibSVMField.setText(avg.formatedBelow(ClassifierType.SVM));

		abIBKField.setText(avg.formatedAbove(ClassifierType.IBK));
		blIBKField.setText(avg.formatedBelow(ClassifierType.IBK));
	}

	private void cleanValues(){
		abJ48Field.setText("");
		blJ48Field.setText("");
		
		abJRipField.setText("");
		blJRipField.setText("");

		abRepTreeField.setText("");
		blRepTreeField.setText("");

		abOneRField.setText("");
		blOneRField.setText("");

		abLibSVMField.setText("");
		blLibSVMField.setText("");

		abIBKField.setText("");
		blIBKField.setText("");
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		rootBox = new VBox();
		
		instanceField = new TextField();
		fileLocationField = readOnlyField();
		
		abJ48Field = readOnlyField();
		blJ48Field = readOnlyField();
		abJRipField = readOnlyField();
		blJRipField = readOnlyField();
		abRepTreeField = readOnlyField();
		blRepTreeField = readOnlyField();
		
		abOneRField = readOnlyField();
		blOneRField = readOnlyField();
		abLibSVMField = readOnlyField();
		blLibSVMField = readOnlyField();
		abIBKField = readOnlyField();
		blIBKField = readOnlyField();
		
		preditor = new Preditor();
	}
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	this.primaryStage = primaryStage;
        primaryStage.setTitle("Dashboard IDEB");
		primaryStage.setScene(new Scene(rootBox, MIN_WIDTH, MIN_HEIGHT));
		primaryStage.resizableProperty().set(false);
    	
    	statusGrid();
    	dataGrid();
    	primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
